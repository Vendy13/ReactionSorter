package com.vendy13.reactionsorter.objects;

import com.vendy13.reactionsorter.enums.FileType;
import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedHashMap;

@Component
public class DirectoryCache {
	
	private final LinkedHashMap<Integer, ReactionObject> directoryCache = new LinkedHashMap<>();
	
	@PostConstruct
	public void fetchDirectoryCache() {
		for (File file : new File("C:\\Users\\Vendy\\Downloads\\medals").listFiles()) {
			String fileName = file.getName();
			String filePath = file.getAbsolutePath();
			String fileExtension = FilenameUtils.getExtension(fileName).toUpperCase();
			FileType fileType = FileType.resolve(fileExtension);
			String fileDimensions = fileType.getDimensions(file); // Placeholder for dimensions, needs implementation
			long fileSize = file.length();
			
			ReactionObject reactionObject = new ReactionObject(fileName, filePath, fileExtension, fileType, fileDimensions, fileSize);
			directoryCache.put(directoryCache.size(), reactionObject);
		}
	}
}
