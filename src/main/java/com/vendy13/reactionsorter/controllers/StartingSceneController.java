package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StartingSceneController {
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DirectoryCache directoryCache;
	
	public void begin(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WorkingScene.fxml"));
		loader.setControllerFactory(context::getBean); // Spring-aware FXMLLoader
		
		Parent root = loader.load();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow(); // TODO inject stage into controller
		Scene scene = new Scene(root);
		
		directoryCache.fetchDirectoryCache();
		
		// TODO Put cache size in label on scene initialization
		stage.setScene(scene);
		stage.show();
	}
}
