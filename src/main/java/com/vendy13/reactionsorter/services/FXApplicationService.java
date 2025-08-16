package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.controllers.StartingSceneController;
import com.vendy13.reactionsorter.utils.FxSpringContextBridge;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class FXApplicationService extends Application {
	@Autowired
	private ApplicationContext context;
	
	@Override
	public void init() {
		context = FxSpringContextBridge.getContext(); // Use injected context
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Image icon = new Image("images/vendyOhNo.png");
		stage.getIcons().add(icon);
		stage.setTitle("Reaction Sorter");
		
		StartingSceneController controller = SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
		controller.init(); // Loads the default directories from preferences
	}
	
	@Override
	public void stop() {
		if (context instanceof AutoCloseable closable) {
			try {
				closable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
