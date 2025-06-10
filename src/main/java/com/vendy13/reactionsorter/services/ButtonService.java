package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.objects.NextCache;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.file.*;

abstract class ButtonService {
	/*
	 * Load file
	 * Move file
	 * Update number
	 * Update fie name field
	 * Load next/prev file
	 * Update cache
	 */
	
	protected DirectoryCache directoryCache;
	protected NextCache nextCache;
	
	// TODO Change to cachedFile
	protected int cachedIndex;
	protected String cachedFileName;
	protected String targetDirectory;
	protected String workingDirectory;
	
	public void loadWorkingFile() {
		// TODO calls index from DirCache
	}
	
	public void moveFile(String fileRename) throws IOException {
//		Files.move(Path.of(workingDirectory + cachedFileName), Path.of(targetDirectory + fileRename), StandardCopyOption.ATOMIC_MOVE);
	}
	
	public void updateNumber(Text workingFileIndex) {
		// TODO updates number in UI via index, maybe combine with filename field update
		workingFileIndex.setText(workingFileIndex.getText().replaceFirst("\\d+", String.valueOf(cachedIndex)));
	}
	
//	abstract void updateFileNameField();
	
//	abstract void loadNextFile();
	// TODO choose next index or previous index based on button pressed
	
//	abstract void updateCache();
}
