package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Service
abstract class ButtonService {
	/*
	 * Load file
	 * Move file
	 * Update number
	 * Update fie name field
	 * Load next/prev file
	 * Update cache
	 */
	
	@Autowired
	protected DirectoryCache directoryCache;
	
	// TODO Change to cachedFile
	protected int cachedIndex = 0;
	protected String cachedFileName;
	protected String targetDirectory;
	protected String workingDirectory;
	
	public void loadWorkingFile(ImageView imageView) throws FileNotFoundException {
		// TODO calls index from DirCache
		ReactionObject workingFile = directoryCache.getDirectoryCache().get(cachedIndex);
		Image image = new Image(new FileInputStream(workingFile.filePath()));
		imageView.setImage(image);
		cachedIndex++;
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
