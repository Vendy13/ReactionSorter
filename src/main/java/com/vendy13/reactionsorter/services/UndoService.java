package com.vendy13.reactionsorter.services;

import com.vendy13.reactionsorter.caches.DirectoryCache;
import com.vendy13.reactionsorter.caches.WorkingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UndoService extends ButtonService {
	/*
	 * Load from Move/Skip cache
	 * Move file
	 * DONE Update number to previous
	 * DONE Load previous file
	 * DONE Update file name field
	 * Unneeded? Clear cache
	 */
	private static final Logger log = LoggerFactory.getLogger(UndoService.class);
	
	UndoService(DirectoryCache directoryCache, WorkingCache workingCache) {
		super(directoryCache, workingCache);
	}
	
	public void undoAction() {
		// TODO move vs skip
		if (workingCache.getIsMove()) {
			undoMove(workingCache.getUndoCache().fileName());
		} else {
			undoSkip();
		}
	}
	
	public void undoMove(String newFileName) {
		// IDEA call moveService.moveFile(cachedFileName)?
	}
	
	public void undoSkip() {
		// IDEA call skipService.skipFile()?
	}
}
