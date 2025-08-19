package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.caches.WorkingCache;
import com.vendy13.reactionsorter.objects.ReactionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SkipService extends ButtonService {
	/*
	 * DONE Load next file
	 * DONE Update number
	 * DONE Update file name field
	 * DONE Cache skipped file for Undo
	 */
	private static final Logger log = LoggerFactory.getLogger(SkipService.class);
	
	SkipService(DirectoryCache directoryCache, WorkingCache workingCache) {
		super(directoryCache, workingCache);
	}
	
	// IDEA maybe just have the index update and have undo do nothing; update isMove flag but that's it
	public void skipFile() {
		// Cache current file state for Undo
//		ReactionObject workingFile = workingCache.getWorkingFile();
		ReactionObject undoCache = workingCache.setUndoCache(workingCache.getWorkingFile());
		workingCache.setIsMove(false);
	}
}
