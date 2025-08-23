package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import com.vendy13.reactionsorter.services.ButtonService;
import com.vendy13.reactionsorter.utils.PreferencesManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

@Component
public class WorkingSceneController implements StageAwareController {
	@FXML
	private Text fileDimensions;
	@FXML
	private Text fileSize;
	@FXML
	private Text fileType;
	@FXML
	private Text directoryCount;
	@FXML
	private Text workingFileIndex;
	@FXML
	private TextField workingDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField fileRename;
	@FXML
	private Button moveButton;
	@FXML
	private Button skipButton;
	@FXML
	private Button undoButton;
	@FXML
	private Button endButton;
	@FXML
	private ImageView imageView;
	@FXML
	private MediaView mediaView;
	
	private static final Logger log = LoggerFactory.getLogger(WorkingSceneController.class);
	
	private final DirectoryCache directoryCache;
	private final PreferencesManager preferencesManager;
	private final ButtonService buttonService;
	
	// TODO Preferences Config
	
	// Cannot undo on first file
	private boolean undoFlag = true;
	private boolean isMove = true;
	private String[] directoryPathsCache;
	private ReactionObject workingFile;
	private ReactionObject undoCache;
	private Stage stage;
	
	@Autowired
	public WorkingSceneController(DirectoryCache directoryCache, PreferencesManager preferencesManager, ButtonService buttonService) {
		this.directoryCache = directoryCache;
		this.preferencesManager = preferencesManager;
		this.buttonService = buttonService;
	}
	
	// IDEA create object to hold all UI elements and pass to services?
	
	// Loads first file
	@FXML
	public void init(String[] directoryPathsCache) throws FileNotFoundException {
		this.directoryPathsCache = directoryPathsCache;
		
		moveButton.setOnAction(event -> move());
		skipButton.setOnAction(event -> skip());
		undoButton.setOnAction(event -> undo());
		endButton.setOnAction(event -> end());
		
		directoryCount.setText(String.valueOf(directoryCache.getDirectoryCache().size()));
		workingDirectory.setText(directoryPathsCache[0]);
		targetDirectory.setText(directoryPathsCache[1]);
		loadWorkingFile();
	}
	
	private void move() {
		// Only stops move if confirmMove is enabled and user selects NO
		if (Boolean.parseBoolean(preferencesManager.getPreference("confirmMove")) &&
				confirm(stage, "Move", "Move file?")) return;
		
		try {
			buttonService.moveFile(fileRename.getText() + "." + workingFile.fileExtension(), directoryPathsCache[1], workingFile);
			undoCache = workingFile;
			
			isMove = true;
		} catch (Exception e) {
			// If move fails, doesn't load next file
			// TODO doesn't work, exception not passed from moveFile()?
			return;
		}
		
		buttonService.endCheck(directoryPathsCache, stage);
		loadWorkingFile();
		undoFlag = false;
	}
	
	private void skip() {
		isMove = false;
		
		buttonService.endCheck(directoryPathsCache, stage);
		loadWorkingFile();
		
		undoFlag = false;
	}
	
	private void undo() {
		// Prevents multiple undos
		if (undoFlag) return;
		
		// IDEA confirm undo?
		directoryCache.previousCachedIndex();
		buttonService.undoMove(isMove, undoCache);
		loadWorkingFile();
		
		undoFlag = true;
	}
	
	private void end() {
		// Ends if YES is selected, continues if NO is selected
		if (confirm(stage, "End", "End sorting?")) return;
		
		directoryCache.setCachedIndex(directoryCache.getDirectoryCache().size() - 1);
		buttonService.endCheck(directoryPathsCache, stage);
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
	private void loadWorkingFile() {
		int cachedIndex = directoryCache.getCachedIndex();
		workingFile = directoryCache.getDirectoryCache().get(cachedIndex);
		
		// TODO Display generic file icon for unsupported file types
		// try-with-resources to ensure FileInputStream is closed and file can be moved
		try (FileInputStream fis = new FileInputStream(workingFile.filePath())) {
			Image image = new Image(fis);
			imageView.setImage(image);
		} catch (Exception e) {
			log.error("Error loading file: {}", e.getMessage());
		}
		
		workingFileIndex.setText(String.valueOf(cachedIndex + 1));
		fileType.setText(workingFile.fileExtension().toUpperCase());
		fileDimensions.setText(workingFile.fileDimensions());
		fileSize.setText(workingFile.fileSize() + "B");
		fileRename.setText(workingFile.fileName());
		
		log.info("Sorting file {} of {}", cachedIndex + 1, directoryCache.getDirectoryCache().size());
	}
	
	private boolean confirm(Stage stage, String action, String message) {
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
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
