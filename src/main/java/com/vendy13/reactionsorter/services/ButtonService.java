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
			/* FIXME Error moving file: The process cannot access the file because it is being used by another process
			     Steps to reproduce:
			     1. Start app & click Begin (initializes Working Scene)
			     2. Click Move (file moves successfully)
			     3. Click End (back to Starting Scene w/ new target directory)
			     4. Choose a different target directory (in Starting Scene)
			     5. Click Begin (back to Working Scene)
			     6. Click Move (error moving file)
			     Bonus: Click Undo and File is (sometimes) "successfully returned" (but likely just passes check of original file location)
			 */
			Files.move(Path.of(workingFile.filePath()), Path.of(newFilePath), StandardCopyOption.ATOMIC_MOVE);
			
			// Cache new file state for Undo
			ReactionObject targetFile = workingFile.move(newFileName, newFilePath);
			directoryCache.getDirectoryCache().put(directoryCache.getCachedIndex(), targetFile);
			
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
	public void endCheck(String[] directoryPathsCache, Stage stage) {
		directoryCache.nextCachedIndex();
		
		if (directoryCache.getCachedIndex() >= directoryCache.getDirectoryCache().size()) {
			log.info("End of directory reached.");
			
			try {
				StartingSceneController controller = SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
				controller.init(directoryPathsCache);
			} catch (IOException e) {
				log.error("Error loading starting scene: {}", e.getMessage());
			}
		}
	}
}
