package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.services.MoveService;
import com.vendy13.reactionsorter.services.SkipService;
import com.vendy13.reactionsorter.services.UndoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
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
	private MoveService moveService;
	@Autowired
	private SkipService skipService;
	@Autowired
	private UndoService undoService;
	@Autowired
	protected DirectoryCache directoryCache;
	
	boolean undoFlag = true; // Cannot undo on first file
	
	public void move(ActionEvent event) throws IOException {
		moveService.moveFile(fileRename.getText());
		directoryCache.nextCachedIndex();
		moveService.loadWorkingFile(imageView);
		moveService.updateUI(fileDimensions, fileSize, fileType, workingFileIndex, fileRename);
		undoFlag = false;
	}
	
	public void skip(ActionEvent event) throws FileNotFoundException {
		directoryCache.nextCachedIndex();
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
}
