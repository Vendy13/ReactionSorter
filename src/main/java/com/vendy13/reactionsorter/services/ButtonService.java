package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import com.vendy13.reactionsorter.utils.PreferencesManager;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
	@Autowired
	protected DirectoryCache directoryCache;
	@Autowired
	protected PreferencesManager preferencesManager;
	
	// TODO private variables and use getters/setters as needed
	// TODO Preference configuration
	protected String targetDirectory;
	protected String workingDirectory;
	protected ReactionObject undoCache;
	
	// TODO MediaView for videos :(
	public void loadWorkingFile(ImageView imageView) throws FileNotFoundException {
		ReactionObject workingFile = directoryCache.getDirectoryCache().get(directoryCache.getCachedIndex());
		Image image = new Image(new FileInputStream(workingFile.filePath()));
		imageView.setImage(image);
	}
	
	// TODO actually move file
	public void moveFile(String newFileName) throws IOException {
		int cachedIndex = directoryCache.getCachedIndex();
		undoCache = directoryCache.getDirectoryCache().get(cachedIndex); // Cache current file state for Undo
//		String newFilePath = targetDirectory + newFileName; // TODO check for end slash
//		Files.move(Path.of(workingFile.filePath()), Path.of(newFilePath), StandardCopyOption.ATOMIC_MOVE);
//		ReactionObject targetFile = new ReactionObject(
//				newFileName,
//				newFilePath,
//				undoCache.fileExtension(),
//				undoCache.fileType(),
//				undoCache.fileDimensions(),
//				undoCache.fileSize()
//		);
//		directoryCache.getDirectoryCache().replace(cachedIndex, targetFile);
	}
	
	public void updateUI(Text fileDimensions, Text fileSize, Text fileType, Text workingFileIndex, TextField fileRename) {
		int cachedIndex = directoryCache.getCachedIndex();
		ReactionObject workingFile = directoryCache.getDirectoryCache().get(cachedIndex);
		workingFileIndex.setText(String.valueOf(cachedIndex + 1));
		fileType.setText(workingFile.fileExtension());
		fileDimensions.setText(workingFile.fileDimensions());
		fileSize.setText(workingFile.fileSize() + "B");
		fileRename.setText(workingFile.fileName());
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
		
		return result.isPresent() && result.get() == ButtonType.NO; // NO to prevent inversion of return value
	}
}
