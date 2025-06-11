package com.vendy13.reactionsorter.objects;

import com.vendy13.reactionsorter.enums.FileType;

public record ReactionObject(
		String fileName,
		String filePath,
		String fileExtension,
		FileType fileType,
		String fileDimensions,
		long fileSize
) {}