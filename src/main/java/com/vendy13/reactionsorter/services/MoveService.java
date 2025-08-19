package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.caches.WorkingCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class MoveService extends ButtonService {
	/*
	 * Rename file
	 * Move file
	 * DONE Update number
	 * DONE Load next file
	 * DONE Update file name field
	 * DONE Cache changes for Undo
	 */
	private static final Logger log = LoggerFactory.getLogger(MoveService.class);
	
	MoveService(DirectoryCache directoryCache, WorkingCache workingCache) {
		super(directoryCache, workingCache);
	}
	
	public void moveFile(String newFileName, String targetDirectory) {
		// Cache current file state for Undo
		ReactionObject workingFile = workingCache.getWorkingFile();
		ReactionObject undoCache = workingCache.setUndoCache(workingCache.getWorkingFile());
		workingCache.setIsMove(true);
		
		try {
			String newFilePath = targetDirectory + newFileName; // TODO check for illegal characters
			Files.move(Path.of(workingFile.filePath()), Path.of(newFilePath), StandardCopyOption.ATOMIC_MOVE);
			ReactionObject targetFile = new ReactionObject(
					newFileName,
					newFilePath,
					undoCache.fileExtension(),
					undoCache.fileType(),
					undoCache.fileDimensions(),
					undoCache.fileSize()
			);
			directoryCache.getDirectoryCache().replace(directoryCache.getCachedIndex(), targetFile);

			log.info("File successfully moved to: {}", newFilePath);
		} catch (Exception e) {
			log.error("Error moving file: {}", e.getMessage());
		}
	}
}
