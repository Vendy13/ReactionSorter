package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.utils.DirectoryUtils;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartingSceneController implements StageAwareController {
	@FXML
	private TextField workingDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private Button workingChoose;
	@FXML
	private Button targetChoose;
	@FXML
	private Button beginButton;
	@FXML
	private Tooltip workingTooltip;
	@FXML
	private Tooltip targetTooltip;
	@FXML
	private MenuItem preferencesMenu;
	
	private static final Logger log = LoggerFactory.getLogger(StartingSceneController.class);
	
	private final ApplicationContext context;
	private final DirectoryCache directoryCache;
	
	// IDEA Cache?
	private String[] directoryPathsCache;
	private Stage stage;
	
	@Autowired
	public StartingSceneController(ApplicationContext context, DirectoryCache directoryCache) {
		this.context = context;
		this.directoryCache = directoryCache;
	}
	
	@FXML
	public void init(String[] directoryPathsCache) {
		this.directoryPathsCache = directoryPathsCache;
		
		workingDirectory.setText(DirectoryUtils.shortenDirectory(directoryPathsCache[0]));
		targetDirectory.setText(DirectoryUtils.shortenDirectory(directoryPathsCache[1]));
		workingTooltip.setText(directoryPathsCache[0]);
		targetTooltip.setText(directoryPathsCache[1]);
		
		preferencesMenu.setOnAction(event -> preferencesMenu());
		beginButton.setOnAction(event -> begin());
		
		workingChoose.setOnAction(event -> this.directoryPathsCache[0] =
				DirectoryUtils.chooseDirectories(false, false, workingDirectory, workingTooltip, stage));
		targetChoose.setOnAction(event -> this.directoryPathsCache[1] =
				DirectoryUtils.chooseDirectories(false, true, targetDirectory, targetTooltip, stage));
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
	
	private void begin() {
		try {
			// Maintains window resize on scene switch
			stage.setWidth(stage.getWidth());
			stage.setHeight(stage.getHeight());
			
			WorkingSceneController controller = SceneLoader.loadScene("/fxml/WorkingScene.fxml", stage, context);
			directoryCache.fetchDirectoryCache(directoryPathsCache[0]);
			controller.init(directoryPathsCache);
		} catch (IOException e) {
			log.error("Error loading working scene: {}", e.getMessage());
		}
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
