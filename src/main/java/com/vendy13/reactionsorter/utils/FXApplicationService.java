package com.vendy13.reactionsorter.utils;

import com.vendy13.reactionsorter.controllers.StartingSceneController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

public class FXApplicationService extends Application {
	private static final Logger log = LoggerFactory.getLogger(FXApplicationService.class);
	
	private ApplicationContext context;
	private PreferencesManager preferencesManager;
	
	@Override
	public void init() {
		// Use injected context
		context = FxSpringContextBridge.getContext();
		
		// Fetch Spring-managed PreferencesManager bean
		preferencesManager = context.getBean(PreferencesManager.class);
		// IDEA WorkingCache here with included directories; intialize them just after
	}
	
	@Override
	public void start(Stage stage) {
		Image icon = new Image("images/vendyOhNo.png");
		stage.getIcons().add(icon);
		stage.setTitle("Reaction Sorter");
		
		try {
			StartingSceneController controller = SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
			
			// Loads the default directories from preferences
			String[] directoryPathsCache = {preferencesManager.getPreference("defaultWorkingDirectory"),
					preferencesManager.getPreference("defaultTargetDirectory")};
			controller.init(directoryPathsCache);
		} catch (IOException e) {
			log.error("Error initializing starting scene: {}", e.getMessage());
		}
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
