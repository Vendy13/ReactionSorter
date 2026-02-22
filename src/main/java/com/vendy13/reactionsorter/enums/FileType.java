package com.vendy13.reactionsorter.enums;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public enum FileType {
	IMAGE {
		@Override
		public String getDimensions(File file) {
			try {
				Dimension dims = getImageDimension(file);
				return dims.width + " x " + dims.height;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
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
	
	/**
	 * <a href="https://stackoverflow.com/a/12164026/17563958">...</a>
	 * Gets image dimensions for given file
	 * @param imgFile image file
	 * @return dimensions of image
	 * @throws IOException if the file is not a known image
	 */
	public static Dimension getImageDimension(File imgFile) throws IOException {
		String extension = FilenameUtils.getExtension(imgFile.getAbsolutePath());
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(extension);
		while(iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(imgFile);
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				return new Dimension(width, height);
			} catch (IOException e) {
				throw new IOException("Error reading: " + imgFile.getAbsolutePath(), e);
			} finally {
				reader.dispose();
			}
		}
		
		throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
	}
}