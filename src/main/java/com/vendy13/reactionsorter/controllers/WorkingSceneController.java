package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.services.MoveService;
import com.vendy13.reactionsorter.services.SkipService;
import com.vendy13.reactionsorter.services.UndoService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkingSceneController {
	
	@FXML
	private Text workingFileIndex;
	
	@Autowired
	private MoveService moveService;
	@Autowired
	private SkipService skipService;
	@Autowired
	private UndoService undoService;
	
	public void move(ActionEvent event) {
		moveService.updateNumber(workingFileIndex);
	}
	
	public void skip(ActionEvent event) {
		skipService.updateNumber(workingFileIndex);
	}
	
	public void undo(ActionEvent event) {
		undoService.updateNumber(workingFileIndex);
	}
}
