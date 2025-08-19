package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.services.EndService;
import com.vendy13.reactionsorter.services.MoveService;
import com.vendy13.reactionsorter.services.SkipService;
import com.vendy13.reactionsorter.services.UndoService;
import com.vendy13.reactionsorter.utils.PreferencesManager;
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
	private Text directoryCount;
	@FXML
	private Text workingFileIndex;
	@FXML
	private TextField workingDirectory;
	@FXML
	private TextField targetDirectory;
	@FXML
	private TextField fileRename;
	@FXML
	private ImageView imageView;
	@FXML
	private MediaView mediaView;
	
	private static final Logger log = LoggerFactory.getLogger(WorkingSceneController.class);
	
	private final DirectoryCache directoryCache;
	private final PreferencesManager preferencesManager;
	private final MoveService moveService;
	private final SkipService skipService;
	private final UndoService undoService;
	private final EndService endService;
	
	// Cannot undo on first file
	private boolean undoFlag = true;
	private String[] directoryPathsCache;
	private Stage stage;
	
	@Autowired
	public WorkingSceneController(DirectoryCache directoryCache, PreferencesManager preferencesManager, MoveService moveService, SkipService skipService, UndoService undoService, EndService endService) {
		this.directoryCache = directoryCache;
		this.preferencesManager = preferencesManager;
		this.moveService = moveService;
		this.skipService = skipService;
		this.undoService = undoService;
		this.endService = endService;
	}
	
	// IDEA create object to hold all UI elements and pass to services?
	
	// Loads first file
	public void init(String[] directoryPathsCache) throws FileNotFoundException {
		this.directoryPathsCache = directoryPathsCache;
		
		directoryCount.setText(String.valueOf(directoryCache.getDirectoryCache().size()));
		workingDirectory.setText(directoryPathsCache[0]);
		targetDirectory.setText(directoryPathsCache[1]);
		skipService.loadWorkingFile(imageView, fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
	}
	
	public void move(ActionEvent event) throws IOException {
		// Only stops move if confirmMove is enabled and user selects NO
		if (Boolean.parseBoolean(preferencesManager.preferences.getProperty("confirmMove")) &&
				moveService.confirm(stage, "Move", "Move file?")) return;
		
		try {
			moveService.moveFile(fileRename.getText(), directoryPathsCache[1]);
		} catch (Exception e) {
			// If move fails, doesn't load next file
			return;
		}
		
		endService.endCheck(directoryPathsCache, stage);
		moveService.loadWorkingFile(imageView, fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void skip(ActionEvent event) throws IOException {
		// IDEA workingCache.setIsMove(false); would be the only place workingCache is used in class
		skipService.skipFile();
		endService.endCheck(directoryPathsCache, stage);
		skipService.loadWorkingFile(imageView, fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void undo(ActionEvent event) throws IOException {
		// Prevents multiple undos
		if (undoFlag) return;
		
		// IDEA confirm undo?
		directoryCache.previousCachedIndex();
		undoService.undoAction();
		undoService.loadWorkingFile(imageView, fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = true;
	}
	
	public void end(ActionEvent event) throws IOException {
		// Ends if YES is selected, continues if NO is selected
		if (endService.confirm(stage, "End", "End sorting?")) return;
		
		directoryCache.setCachedIndex(directoryCache.getDirectoryCache().size() - 1);
		endService.endCheck(directoryPathsCache, stage);
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
