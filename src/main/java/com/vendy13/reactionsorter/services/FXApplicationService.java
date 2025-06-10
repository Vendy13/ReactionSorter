package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.utils.FxSpringContextBridge;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class FXApplicationService extends Application {
	
	private ApplicationContext context;
	
	@Override
	public void init() {
		context = FxSpringContextBridge.getContext(); // Use injected context
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WorkingScene.fxml"));
		loader.setControllerFactory(context::getBean); // Use Spring for @Autowired controllers
		Parent root = loader.load();
		Scene scene = new Scene(root, 800, 600, Color.GRAY);
		Image favicon = new Image(getClass().getResourceAsStream("/images/vendyOhNo.png"));

		stage.setTitle("Reaction Sorter");
		stage.getIcons().add(favicon);
		stage.setScene(scene);
		stage.show();
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
