package com.vendy13.reactionsorter.services;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;

abstract class ButtonService {
	/*
	 * Load file
	 * Move file
	 * Update number
	 * Update fie name field
	 * Load next/prev file
	 * Update cache
	 */
	
	protected int cachedIndex;
	
	public void loadWorkingFile() {
		// TODO calls index from DirCache
	}
	
	public void moveFile(Path source, Path target) throws IOException {
		Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
	}
	
	public void updateNumber(Text workingFileIndex) {
		// TODO updates number in UI via index, maybe combine with filename field update
		String[] oldText = workingFileIndex.getText().split(" ");
		oldText[0] = String.valueOf(cachedIndex);
		workingFileIndex.setText(Arrays.toString(oldText));
	}
	
//	abstract void updateFileNameField();
	
//	abstract void loadNextFile();
	// TODO choose next index or previous index based on button pressed
	
//	abstract void updateCache();
}
