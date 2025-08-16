package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.objects.DirectoryCache;
import com.vendy13.reactionsorter.utils.SceneLoader;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EndService {
	@Autowired
	private ApplicationContext context;
	@Autowired
	private DirectoryCache directoryCache;
	
	private static final Logger log = LoggerFactory.getLogger(EndService.class);
	
	public void endCheck(Stage stage) throws IOException {
		if (directoryCache.getCachedIndex() >= directoryCache.getDirectoryCache().size() - 1) {
			log.info("End of directory reached.");
			
			SceneLoader.loadScene("/fxml/StartingScene.fxml", stage, context);
		}
	}
	
	// TODO Confirm end (new modal scene?)
	public void confirmEnd() {}
}
