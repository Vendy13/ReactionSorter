package com.vendy13.reactionsorter.caches;

import com.vendy13.reactionsorter.objects.ReactionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WorkingCache {
	private static final Logger log = LoggerFactory.getLogger(WorkingCache.class);
	
	private boolean isMove;
	private ReactionObject workingFile;
	private ReactionObject undoCache;
	
	
	public boolean getIsMove() {
		return isMove;
	}
	
	public void setIsMove(boolean isMove) {
		this.isMove = isMove;
	}
	
	public ReactionObject getWorkingFile() {
		return workingFile;
	}
	
	public ReactionObject setWorkingFile(ReactionObject workingFile) {
		this.workingFile = workingFile;
		return this.workingFile;
	}
	
	public ReactionObject getUndoCache() {
		return undoCache;
	}
	
	public ReactionObject setUndoCache(ReactionObject undoCache) {
		this.undoCache = undoCache;
		return this.undoCache;
	}
}
