package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.controllers.StartingSceneController;
import com.vendy13.reactionsorter.objects.ReactionObject;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ButtonService {
	private static final Logger log = LoggerFactory.getLogger(ButtonService.class);
	
	private final ApplicationContext context;
	private final DirectoryCache directoryCache;
	
	@Autowired
	ButtonService(ApplicationContext context, DirectoryCache directoryCache) {
		this.context = context;
		this.directoryCache = directoryCache;
	}
	
	public void moveFile(String newFileName, String targetDirectory, ReactionObject workingFile) {
		try {
			String newFilePath = Path.of(targetDirectory, newFileName).toString(); // TODO check for illegal characters
			Files.move(Path.of(workingFile.filePath()), Path.of(newFilePath), StandardCopyOption.ATOMIC_MOVE);
			
			// Cache new file state for Undo
			ReactionObject targetFile = workingFile.move(newFileName, newFilePath);
			directoryCache.getDirectoryCache().replace(directoryCache.getCachedIndex(), targetFile);
			
			log.info("File successfully moved to: {}", newFilePath);
		} catch (Exception e) {
			log.error("Error moving file: {}", e.getMessage());
		}
	}
	
	public void undoMove(boolean isMove, ReactionObject undoCache) {
		if (isMove) {
			try {
				ReactionObject movedFile = directoryCache.getDirectoryCache().get(directoryCache.getCachedIndex());
				String oldFilePath = undoCache.filePath();
				Files.move(Path.of(movedFile.filePath()), Path.of(oldFilePath), StandardCopyOption.ATOMIC_MOVE);

				directoryCache.getDirectoryCache().replace(directoryCache.getCachedIndex(), undoCache);
				log.info("File successfully returned to: {}", oldFilePath);
			} catch (Exception e) {
				log.error("Error undoing file move: {}", e.getMessage());
			}
		}
	}
	
	// IDEA add something in case of needing final undo
	public void endCheck(String[] directoryPathsCache, Stage stage) throws IOException {
		directoryCache.nextCachedIndex();
		
		if (directoryCache.getCachedIndex() >= directoryCache.getDirectoryCache().size() - 1) {
			log.info("End of directory reached.");
			
			StartingSceneController controller = SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
			controller.init(directoryPathsCache);
		}
	}
}
