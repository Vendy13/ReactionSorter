package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.services.MoveService;
import com.vendy13.reactionsorter.services.SkipService;
import com.vendy13.reactionsorter.services.UndoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WorkingSceneController {
	@FXML
	private Text fileDimensions;
	@FXML
	private Text fileSize;
	@FXML
	private Text fileType;
	@FXML
	private Text workingFileIndex;
	@FXML
	private TextField fileRename;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField workingDirectory;
	@FXML
	private ImageView imageView;
	@FXML
	private MediaView mediaView;
	
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DirectoryCache directoryCache;
	@Autowired
	private MoveService moveService;
	@Autowired
	private SkipService skipService;
	@Autowired
	private UndoService undoService;
	
	private static final Logger log = LoggerFactory.getLogger(DirectoryCache.class);
	
	private boolean undoFlag = true; // Cannot undo on first file
	
	public void move(ActionEvent event) throws IOException {
		moveService.moveFile(fileRename.getText());
		directoryCache.nextCachedIndex();
		endCheck();
		moveService.loadWorkingFile(imageView);
		moveService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void skip(ActionEvent event) throws IOException {
		directoryCache.nextCachedIndex();
		endCheck();
		skipService.loadWorkingFile(imageView);
		skipService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void undo(ActionEvent event) throws IOException {
		if (undoFlag) return; // Prevent multiple undos
		directoryCache.previousCachedIndex();
		undoService.moveFile(fileRename.getText());
		undoService.loadWorkingFile(imageView);
		undoService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = true;
	}
	
	public void end(ActionEvent event) throws IOException {
		// TODO Confirm end
		directoryCache.setCachedIndex(directoryCache.getDirectoryCache().size() - 1);
		endCheck();
	}
	
	public void endCheck() throws IOException {
		if (directoryCache.getCachedIndex() >= directoryCache.getDirectoryCache().size() - 1) {
			log.info("End of directory reached.");
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StartingScene.fxml"));
			loader.setControllerFactory(context::getBean); // Spring-aware FXMLLoader
			
			Parent root = loader.load();
			Stage stage = (Stage)workingFileIndex.getScene().getWindow(); // TODO inject stage into controller
			Scene scene = new Scene(root);
			
			stage.setScene(scene);
			stage.show();
		}
	}
}
