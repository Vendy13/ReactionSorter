package com.vendy13.reactionsorter.caches;

import com.vendy13.reactionsorter.enums.FileType;
import com.vendy13.reactionsorter.objects.ReactionObject;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedHashMap;

@Component
public class DirectoryCache {
	private static final Logger log = LoggerFactory.getLogger(DirectoryCache.class);
	
	private int cachedIndex;
	private LinkedHashMap<Integer, ReactionObject> directoryCache;
	
	public void fetchDirectoryCache(String workingDirectory) {
		cachedIndex = 0; // Reset index on new fetch
		directoryCache = new LinkedHashMap<>();
		
		for (File file : new File(workingDirectory).listFiles()) {
			String fileName = file.getName();
			String filePath = file.getAbsolutePath();
			String fileExtension = FilenameUtils.getExtension(fileName).toUpperCase();
			FileType fileType = FileType.resolve(fileExtension);
			String fileDimensions = fileType.getDimensions(file); // TODO Placeholder for dimensions, needs implementation
			long fileSize = file.length();
			
			ReactionObject reactionObject = new ReactionObject(fileName, filePath, fileExtension, fileType, fileDimensions, fileSize);
			directoryCache.put(directoryCache.size(), reactionObject);
		}
		
		log.info("Directory cached with {} files.", directoryCache.size());
	}
	
	public LinkedHashMap<Integer, ReactionObject> getDirectoryCache() {
		return directoryCache;
	}
	
	public int getCachedIndex() {
		return cachedIndex;
	}
	
	public void setCachedIndex(int cachedIndex) {
		if (cachedIndex >= 0 && cachedIndex < directoryCache.size()) {
			this.cachedIndex = cachedIndex;
		} else {
			throw new IndexOutOfBoundsException("Cached index out of bounds: " + cachedIndex);
		}
	}
	
	public void nextCachedIndex() {
		cachedIndex++;
	}
	
	public void previousCachedIndex() {
		if (cachedIndex > 0) {
			cachedIndex--;
		}
	}
}
