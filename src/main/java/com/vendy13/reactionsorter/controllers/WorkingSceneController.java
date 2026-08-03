package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.enums.FileType;
import com.vendy13.reactionsorter.objects.ReactionObject;
import com.vendy13.reactionsorter.services.ButtonService;
import com.vendy13.reactionsorter.utils.DirectoryUtils;
import com.vendy13.reactionsorter.utils.PreferencesManager;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurface;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;
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
	private Text fileIndex;
	@FXML
	private TextField workingDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField fileRename;
	@FXML
	private Button targetChoose;
	@FXML
	private Button moveButton;
	@FXML
	private Button skipButton;
	@FXML
	private Button undoButton;
	@FXML
	private Button endButton;
	@FXML
	private Tooltip workingTooltip;
	@FXML
	private Tooltip targetTooltip;
	@FXML
	private MenuItem preferencesMenu;
	@FXML
	private ImageView imageView;
	@FXML
	private StackPane stackPane;
	
	private static final Logger log = LoggerFactory.getLogger(WorkingSceneController.class);
	
	private final ApplicationContext context;
	private final DirectoryCache directoryCache;
	private final PreferencesManager preferencesManager;
	private final ButtonService buttonService;
	private final MediaPlayerFactory mediaPlayerFactory;
	private final EmbeddedMediaPlayer embeddedMediaPlayer;
	
	// Cannot undo on first file
	private boolean undoFlag = true;
	private boolean isMove = true;
	private String[] directoryPathsCache;
	private ReactionObject workingFile;
	private ReactionObject undoCache;
	private Stage stage;
	
	@Autowired
	public WorkingSceneController(ApplicationContext context, DirectoryCache directoryCache, PreferencesManager preferencesManager, ButtonService buttonService) {
		this.context = context;
		this.directoryCache = directoryCache;
		this.preferencesManager = preferencesManager;
		this.buttonService = buttonService;
		
		this.mediaPlayerFactory = new MediaPlayerFactory();
		this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
		this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void playing(MediaPlayer mediaPlayer) {}
			
			@Override
			public void paused(MediaPlayer mediaPlayer) {}
			
			@Override
			public void stopped(MediaPlayer mediaPlayer) {}
			
			@Override
			public void timeChanged(MediaPlayer mediaPlayer, long newTime) {}
		});
	}
	
	// IDEA create object to hold all UI elements and pass to services?
	
	// Loads first file
	@FXML
	public void init(String[] directoryPathsCache) {
		this.directoryPathsCache = directoryPathsCache;
		
		embeddedMediaPlayer.videoSurface().set(new ImageViewVideoSurface(this.imageView));
		
		// Resizes image with window
		// IDEA button to toggle original resolution (ScrollPane if exceeds StackPane size)
		imageView.fitWidthProperty().bind(stackPane.widthProperty());
		imageView.fitHeightProperty().bind(stackPane.heightProperty());
		
		workingDirectory.setText(DirectoryUtils.shortenDirectory(directoryPathsCache[0]));
		targetDirectory.setText(DirectoryUtils.shortenDirectory(directoryPathsCache[1]));
		workingTooltip.setText(directoryPathsCache[0]);
		targetTooltip.setText(directoryPathsCache[1]);
		
		preferencesMenu.setOnAction(event -> preferencesMenu());
		moveButton.setOnAction(event -> move());
		skipButton.setOnAction(event -> skip());
		undoButton.setOnAction(event -> undo());
		endButton.setOnAction(event -> end());

		targetChoose.setOnAction(event -> this.directoryPathsCache[1] =
				DirectoryUtils.chooseDirectories(false, true, targetDirectory, targetTooltip, stage));
		
		loadWorkingFile();
	}
	
	private void preferencesMenu() {
		try {
			Stage prefsStage = new Stage();
			prefsStage.setTitle("Preferences");
			prefsStage.setResizable(false);
			prefsStage.initModality(Modality.APPLICATION_MODAL);
			prefsStage.initOwner(stage);
			
			PreferencesModalController controller = SceneLoader.loadScene("/fxml/PreferencesModal.fxml",
					prefsStage, context);
			controller.init();
			
			prefsStage.showAndWait();
		} catch (IOException e) {
			log.error("Error loading preferences scene: {}", e.getMessage());
		}
	}
	
	private void move() {
		// Only stops move if confirmMove is enabled and user selects NO
		if (Boolean.parseBoolean(preferencesManager.getPreference("confirmMove")) &&
				confirm(stage, "Move", "Move file?")) return;
		
		try {
			buttonService.moveFile(fileRename.getText() + "." + workingFile.fileExtension(),
					directoryPathsCache[1], workingFile);
			undoCache = workingFile;
			
			isMove = true;
		} catch (Exception e) {
			// If move fails, doesn't load next file
			// TODO doesn't work, exception not passed from moveFile()?
			log.error("Error error moving file: {}", e.getMessage());
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
		
		// Closes down vlcj components
		embeddedMediaPlayer.controls().stop();
		embeddedMediaPlayer.release();
		mediaPlayerFactory.release();
		
		directoryCache.setCachedIndex(directoryCache.getDirectoryCache().size() - 1);
		buttonService.endCheck(directoryPathsCache, stage);
	}
	
	private void loadWorkingFile() {
		embeddedMediaPlayer.controls().stop();
		
		int cachedIndex = directoryCache.getCachedIndex();
		workingFile = directoryCache.getDirectoryCache().get(cachedIndex);
		
		if (workingFile.fileType() == FileType.IMAGE) {
			// try-with-resources to ensure FileInputStream is closed and file can be moved
			try (FileInputStream fis = new FileInputStream(workingFile.filePath())) {
				Image image = new Image(fis);
				imageView.setImage(image);
			} catch (Exception e) {
				log.error("Error loading image file: {}", e.getMessage());
			}
		} else if (workingFile.fileType() == FileType.VIDEO) {
			try {
				embeddedMediaPlayer.media().play(workingFile.filePath());
			} catch (Exception e) {
				log.error("Error loading video file: {}", e.getMessage());
			}
		} else {
			Image defaultIcon = new Image("images/default_file.png");
			imageView.setImage(defaultIcon);
		}
		
		fileIndex.setText(cachedIndex + 1 + " of " + directoryCache.getDirectoryCache().size());
		fileType.setText(workingFile.fileExtension().toUpperCase());
		fileDimensions.setText(workingFile.fileDimensions());
		fileSize.setText(workingFile.fileSize());
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
