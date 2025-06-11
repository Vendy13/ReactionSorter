package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import javafx.scene.control.TextField;
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
	 * Update file name field
	 * Load next/prev file
	 * Update cache
	 */
	
	@Autowired
	protected DirectoryCache directoryCache;
	
	protected ReactionObject undoCache;
	
	// TODO Preference configuration
	protected String targetDirectory;
	protected String workingDirectory;
	
	// TODO MediaView for videos :(
	public void loadWorkingFile(ImageView imageView) throws FileNotFoundException {
		ReactionObject workingFile = directoryCache.getDirectoryCache().get(directoryCache.getCachedIndex());
		Image image = new Image(new FileInputStream(workingFile.filePath()));
		imageView.setImage(image);
	}
	
	public void moveFile(String newFileName) throws IOException {
		int cachedIndex = directoryCache.getCachedIndex();
		undoCache = directoryCache.getDirectoryCache().get(cachedIndex); // Cache current file state for Undo
//		String newFilePath = targetDirectory + newFileName; // TODO check for end slash
//		Files.move(Path.of(workingFile.filePath()), Path.of(newFilePath), StandardCopyOption.ATOMIC_MOVE);
//		ReactionObject targetFile = new ReactionObject(
//				newFileName,
//				newFilePath,
//				undoCache.fileExtension(),
//				undoCache.fileType(),
//				undoCache.fileDimensions(),
//				undoCache.fileSize()
//		);
//		directoryCache.getDirectoryCache().replace(cachedIndex, targetFile);
	}
	
	public void updateUI(Text fileDimensions, Text fileSize, Text fileType, Text workingFileIndex, TextField fileRename) {
		int cachedIndex = directoryCache.getCachedIndex();
		ReactionObject workingFile = directoryCache.getDirectoryCache().get(cachedIndex);
		workingFileIndex.setText(workingFileIndex.getText().replaceFirst("\\d+", String.valueOf(cachedIndex + 1)));
		fileType.setText(workingFile.fileExtension());
		fileDimensions.setText(workingFile.fileDimensions());
		fileSize.setText(workingFile.fileSize() + "B");
		fileRename.setText(workingFile.fileName());
	}
}
