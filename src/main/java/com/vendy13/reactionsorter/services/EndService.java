package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.caches.WorkingCache;
import com.vendy13.reactionsorter.controllers.StartingSceneController;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EndService extends ButtonService {
	private static final Logger log = LoggerFactory.getLogger(EndService.class);
	
	private final ApplicationContext context;
	
	@Autowired
	public EndService(DirectoryCache directoryCache, WorkingCache workingCache, ApplicationContext context) {
		super(directoryCache, workingCache);
		this.context = context;
	}
	
	public void endCheck(String[] directoryPathsCache, Stage stage) throws IOException {
		directoryCache.nextCachedIndex();
		
		if (directoryCache.getCachedIndex() >= directoryCache.getDirectoryCache().size() - 1) {
			log.info("End of directory reached.");
			
			StartingSceneController controller = SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
			controller.init(directoryPathsCache);
		}
	}
}
