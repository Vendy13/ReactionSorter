package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.services.EndService;
import com.vendy13.reactionsorter.services.MoveService;
import com.vendy13.reactionsorter.services.SkipService;
import com.vendy13.reactionsorter.services.UndoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class WorkingSceneController implements StageAwareController {
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
	@Autowired
	private EndService endService;
	
	private static final Logger log = LoggerFactory.getLogger(WorkingSceneController.class);
	
	private Stage stage;
	private boolean undoFlag = true; // Cannot undo on first file
	
	// Loads first file
	public void init() throws FileNotFoundException {
		skipService.loadWorkingFile(imageView);
		skipService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
	}
	
	public void move(ActionEvent event) throws IOException {
		moveService.moveFile(fileRename.getText());
		directoryCache.nextCachedIndex();
		endService.endCheck(stage);
		moveService.loadWorkingFile(imageView);
		moveService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void skip(ActionEvent event) throws IOException {
		directoryCache.nextCachedIndex();
		endService.endCheck(stage);
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
		// TODO Save current target directory in prefs
		directoryCache.setCachedIndex(directoryCache.getDirectoryCache().size() - 1);
		endService.endCheck(stage);
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
