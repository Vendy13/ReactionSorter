package com.vendy13.reactionsorter.enums;

import java.io.File;
import java.util.Map;

public enum FileType {
	IMAGE {
		@Override
		public String getDimensions(File file) {
//			return new ImageUtils.getImageDimensions(file);
			return "Image Dims";
		}
	},
	VIDEO {
		@Override
		public String getDimensions(File file) {
//			return new VideoUtils.getVideoDimensions(file);
			return "Video Dims";
		}
	},
	OTHER {
		@Override
		public String getDimensions(File file) {
			return null;
		}
	};
	
	public abstract String getDimensions(File file);
	
	private static final Map<String, FileType> EXT_TYPE_MAP = Map.ofEntries(
			Map.entry("JPG", IMAGE),
			Map.entry("JPEG", IMAGE),
			Map.entry("PNG", IMAGE),
			Map.entry("GIF", IMAGE),
			Map.entry("MP4", VIDEO),
			Map.entry("WEBM", VIDEO)
	);
	
	public static FileType resolve(String extension) {
		return EXT_TYPE_MAP.getOrDefault(extension.toUpperCase(), OTHER);
	}
}