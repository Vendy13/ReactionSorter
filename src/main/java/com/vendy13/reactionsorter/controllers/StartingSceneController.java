package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
	
	public void init(String[] directoryPathsCache) {
		this.directoryPathsCache = directoryPathsCache;
		
		// TODO shorten directory paths for display
		workingDirectory.setText(directoryPathsCache[0]);
		targetDirectory.setText(directoryPathsCache[1]);
	}
	
	public void begin(ActionEvent event) throws IOException {
		WorkingSceneController controller = SceneLoader.loadScene("/fxml/WorkingScene.fxml", stage, context);
		
		directoryCache.fetchDirectoryCache(directoryPathsCache[0]);
		controller.init(directoryPathsCache);
	}
	
	public void preferencesMenu(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PreferencesModal.fxml"));
		Parent root = loader.load();
		
		Stage prefsStage = new Stage();
		prefsStage.setTitle("Preferences");
		prefsStage.setScene(new Scene(root));
		prefsStage.initModality(Modality.APPLICATION_MODAL);
		prefsStage.initOwner(stage);
		
		prefsStage.showAndWait();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
