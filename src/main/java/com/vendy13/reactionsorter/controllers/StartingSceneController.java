package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartingSceneController implements StageAwareController {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DirectoryCache directoryCache;
	
	private Stage stage;
	
	public void begin(ActionEvent event) throws IOException {
		WorkingSceneController controller = SceneLoader.loadScene("/fxml/WorkingScene.fxml", stage, context);
		
		// TODO Put cache size in label on scene initialization
		directoryCache.fetchDirectoryCache();
		controller.init();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
