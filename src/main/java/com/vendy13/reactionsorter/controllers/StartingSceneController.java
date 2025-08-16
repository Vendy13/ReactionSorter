package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.utils.PreferencesManager;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
		workingDirectory.setText(preferencesManager.preferences.getProperty("defaultWorkingDirectory"));
		targetDirectory.setText(preferencesManager.preferences.getProperty("defaultTargetDirectory"));
	}
	
	public void begin(ActionEvent event) throws IOException {
		WorkingSceneController controller = SceneLoader.loadScene("/fxml/WorkingScene.fxml", stage, context);
		
		directoryCache.fetchDirectoryCache();
		controller.init();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
