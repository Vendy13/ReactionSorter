package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.utils.FxSpringContextBridge;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
		
		Text text = new Text();
		text.setText("Choose");
		text.setX(100);
		text.setY(100);
		text.setFill(Color.BLACK);
		text.setFont(Font.font(50));
		
		ImageView imageView = new ImageView();
//		Image currentImage = new Image(getClass().getResourceAsStream(workingDirectory + currentFile)); TODO method(?) to select current file
//		imageView.setImage(image);
		
//		root.getChildren().add(text);
//		root.getChildren().add(text);
		
		stage.setTitle("Reaction Sorter");
		stage.getIcons().add(favicon);
//		stage.setWidth(800);
//		stage.setHeight(600);
//		stage.setMaximized(true);
		
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
//	@PostConstruct
//	public void runApp() {
//		// This method is called after the JavaFX application has been initialized.
//		// You can perform any setup or configuration here if needed.
//		System.out.println("Reaction Sorter application initialized.");
//		launch();
//	}
}
