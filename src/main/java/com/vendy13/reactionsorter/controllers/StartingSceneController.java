package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.utils.PreferencesManager;
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
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DirectoryCache directoryCache;
	@Autowired
	private PreferencesManager preferencesManager;
	
	private Stage stage;
	
	public void init() {
		// TODO shorten directory paths for display
		workingDirectory.setText(preferencesManager.preferences.getProperty("defaultWorkingDirectory"));
		targetDirectory.setText(preferencesManager.preferences.getProperty("defaultTargetDirectory"));
	}
	
	public void begin(ActionEvent event) throws IOException {
		WorkingSceneController controller = SceneLoader.loadScene("/fxml/WorkingScene.fxml", stage, context);
		
		directoryCache.fetchDirectoryCache();
		controller.init();
	}
	
	public void preferencesMenu(ActionEvent event) throws IOException {
//		SceneLoader.loadScene("/fxml/PreferencesModal.fxml", stage, context);
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
