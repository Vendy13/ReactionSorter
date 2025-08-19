package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.caches.WorkingCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Optional;

@Service
abstract class ButtonService {
	/*
	 * Load file
	 * Move file
	 * Update number
	 * Update file name field
	 * Load next/prev file
	 * Update cache
	 */
	private static final Logger log = LoggerFactory.getLogger(ButtonService.class);
	
	protected final DirectoryCache directoryCache;
	protected final WorkingCache workingCache;
	
	ButtonService(DirectoryCache directoryCache, WorkingCache workingCache) {
		this.directoryCache = directoryCache;
		this.workingCache = workingCache;
	}
	
	/* TODO MediaView for videos :(
	 * MEDIAVIEW WILL HAVE TO BE LOADED IN A DIFFERENT WAY
	 * Current option outlined below:
	 * mediaPlayer.stop()
	 * mediaPlayer.dispose()
	 * Media is media file/stream
	 * MediaPlayer is the actual player that maintains the file in use
	 * MediaView is just the visual display component for the UI
	 *
	 * Alternate option using temp files, maybe be sluggish:
	 * Create temp directory within same place as local preferences file
	 * Copy video file to temp directory
	 * Load MediaView with file from temp directory
	 * Real file can be moved and renamed without being locked
	 * Delete temp file when move is complete
	 */
	public void loadWorkingFile(ImageView imageView, Text fileDimensions, Text fileSize, Text fileType, Text workingFileIndex, TextField fileRename) {
		int cachedIndex = directoryCache.getCachedIndex();
		ReactionObject workingFile = workingCache.setWorkingFile(directoryCache.getDirectoryCache().get(cachedIndex));
		
		// try-with-resources to ensure FileInputStream is closed and file can be moved
		try (FileInputStream fis = new FileInputStream(workingFile.filePath())) {
			Image image = new Image(fis);
			imageView.setImage(image);
		} catch (Exception e) {
			log.error("Error loading file: {}", e.getMessage());
		}
		
		workingFileIndex.setText(String.valueOf(cachedIndex + 1));
		fileType.setText(workingFile.fileExtension());
		fileDimensions.setText(workingFile.fileDimensions());
		fileSize.setText(workingFile.fileSize() + "B");
		fileRename.setText(workingFile.fileName());
		
		log.info("Working on file {} of {}", cachedIndex + 1, directoryCache.getDirectoryCache().size());
	}
	
	public boolean confirm(Stage stage, String action, String message) {
		Alert confirm = new Alert(Alert.AlertType.INFORMATION);
		confirm.setTitle("Confirm " + action);
		confirm.setHeaderText(null);
		confirm.setGraphic(null);
		confirm.setContentText(message);
		confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		
		// Modifies innate values of Alert stages
		Platform.runLater(() -> {
			Stage confirmStage = (Stage) confirm.getDialogPane().getScene().getWindow();
			confirmStage.setIconified(false);
			confirmStage.setWidth(230);
			confirmStage.setHeight(120);
			confirmStage.setX(stage.getX() + stage.getWidth()/2 - confirmStage.getWidth()/2);
			confirmStage.setY(stage.getY() + stage.getHeight()/2 - confirmStage.getHeight()/2);
		});
		
		Optional<ButtonType> result = confirm.showAndWait();
		
		// NO to prevent inversion of return value
		return result.isPresent() && result.get() == ButtonType.NO;
	}
}
