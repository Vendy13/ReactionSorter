package com.vendy13.reactionsorter.objects;

import com.vendy13.reactionsorter.enums.FileType;

public record ReactionObject(
		String fileName,
		String filePath,
		String fileExtension,
		FileType fileType,
		String fileDimensions,
		long fileSize
) {
	public ReactionObject move(String newFileName, String newFilePath) {
		return new ReactionObject(
				newFileName,
				newFilePath,
				this.fileExtension,
				this.fileType,
				this.fileDimensions,
				this.fileSize
		);
	}
}