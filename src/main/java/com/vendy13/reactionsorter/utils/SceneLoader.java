package com.vendy13.reactionsorter.utils;

import com.vendy13.reactionsorter.controllers.StageAwareController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SceneLoader {
	public static <T> T loadScene(String fxmlPath, Stage stage, ApplicationContext context) throws IOException {
		FXMLLoader loader = new FXMLLoader(SceneLoader.class.getResource(fxmlPath));
		loader.setControllerFactory(context::getBean); // Spring-aware FXMLLoader
		Parent root = loader.load();
		T controller = loader.getController();
		
		// Inject Stage into the controller
		if (controller instanceof StageAwareController) {
			((StageAwareController) controller).setStage(stage);
		}
		
		stage.setScene(new Scene(root));
		stage.show();
		
		return controller;
	}
}
