package com.vendy13.reactionsorter.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class PreferencesFileManager {
	private static final Logger log = LoggerFactory.getLogger(PreferencesFileManager.class);
	
	public static final String OS = System.getProperty("os.name");
	public static final String APP_DATA = System.getenv("LOCALAPPDATA");
	public static final String USER_HOME = System.getProperty("user.home");
	
	private Path prefPath;
	
	@PostConstruct
	public void init() throws IOException {
		setOS();
		createLocalPreferences();
	}
	
	public void setOS() {
		if (StringUtils.containsIgnoreCase(OS, "win")) {
			prefPath = Paths.get(APP_DATA, "ReactionSorter", "preferences.properties");
		} else if (StringUtils.containsIgnoreCase(OS,"mac")) {
			prefPath = Paths.get(USER_HOME, "Library", "Application Support", "ReactionSorter", "preferences.properties");
		} else if (StringUtils.containsIgnoreCase(OS, "nix") || StringUtils.containsIgnoreCase(OS, "nux")) {
			prefPath = Paths.get(USER_HOME, ".config", "ReactionSorter", "preferences.properties");
		} else {
			throw new UnsupportedOperationException("Unsupported OS: " + OS);
		}
		
		log.info("OS detected: {}", OS);
	}
	
	public void createLocalPreferences() {
		InputStream defaultPrefs = ClassLoader.getSystemResourceAsStream("preferences.properties");
		
		try {
			if (!Files.isDirectory(prefPath.getParent())) {
				Files.createDirectories(prefPath.getParent());
				log.info("ReactionSorter directory created at: {}", prefPath.getParent());
			}
			if (!Files.exists(prefPath) && defaultPrefs != null) {
				Files.copy(defaultPrefs, prefPath);
				log.info("Preferences file created at: {}", prefPath);
			}
		} catch (Exception e) {
			log.error("Error creating local preferences file: {}", e.getMessage());
		}
	}
}
