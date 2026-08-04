package com.vendy13.reactionsorter.enums;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.media.*;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public enum FileType {
	IMAGE {
		@Override
		public String getDimensions(File file) {
			try {
				Dimension dims = getImageDimension(file);
				return dims.height + " x " + dims.width;
			} catch (IOException e) {
				log.error("Error getting image dimensions: {}", e.getMessage());
				return "Image Dims";
			}
		}
	},
	VIDEO {
		@Override
		public String getDimensions(File file) {
			// Waits for each video to fully parse in order to grab dims
			CountDownLatch latch = new CountDownLatch(1);
			Media media = mediaPlayerFactory.media().newMedia(file.getAbsolutePath());
			media.events().addMediaEventListener(new MediaEventAdapter() {
				@Override
				public void mediaParsedChanged(Media media, MediaParsedStatus status) {
					if (status == MediaParsedStatus.DONE) {
						latch.countDown();
					};
				}
			});
			
			try {
				if (media.parsing().parse()) {
					latch.await();
					for (TrackInfo track : media.info().tracks()) {
						if (track instanceof VideoTrackInfo) {
							return ((VideoTrackInfo) track).height() + " x " + ((VideoTrackInfo) track).width();
						}
					}
				}
				return "Video Dims";
			} catch (Exception e) {
				log.error("Error getting video dimensions: {}", e.getMessage());
				return "Error Dims";
			} finally {
				media.release();
			}
		}
	},
	OTHER {
		@Override
		public String getDimensions(File file) {
			return "N/A";
		}
	};
	
	private static final Logger log = LoggerFactory.getLogger(FileType.class);
	private static final MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
	private static final Map<String, FileType> EXT_TYPE_MAP = Map.ofEntries(
			Map.entry("BMP", IMAGE),
			Map.entry("JPG", IMAGE),
			Map.entry("JPEG", IMAGE),
			Map.entry("PNG", IMAGE),
			Map.entry("GIF", IMAGE),
			Map.entry("MP4", VIDEO),
			Map.entry("WEBM", VIDEO)
	);
	
	public abstract String getDimensions(File file);
	
	public static FileType resolve(String extension) {
		return EXT_TYPE_MAP.getOrDefault(extension.toUpperCase(), OTHER);
	}
	
	// IDEA TwelveMonkeys ImageIO plugin to support more formats and get dimensions without loading image into memory
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
		
		while (iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(imgFile);
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				
				stream.close();
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