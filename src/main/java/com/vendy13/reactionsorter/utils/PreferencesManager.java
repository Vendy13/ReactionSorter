package com.vendy13.reactionsorter.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@Component
public class PreferencesManager {
	private static final Logger log = LoggerFactory.getLogger(PreferencesManager.class);
	private static final String OS = System.getProperty("os.name");
	private static final String APP_DATA = System.getenv("LOCALAPPDATA");
	private static final String USER_HOME = System.getProperty("user.home");
	
	private InputStream defaultPrefs = ClassLoader.getSystemResourceAsStream("preferences.properties");
	private Path prefPath;
	
	public Properties preferences = new Properties();
	
	@PostConstruct
	public void init() {
		setOS();
		createLocalPreferences();
		loadPreferences();
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
		
		log.info("Preferences file located at: {}", prefPath);
	}
	
	private void loadPreferences() {
		try {
			if (Files.exists(prefPath)) {
				try {
					InputStream prefs = Files.newInputStream(prefPath);
					preferences.load(prefs);
					log.info("Preferences successfully loaded");
				} catch (Exception e) {
					log.error("Error reading preferences file: {}", e.getMessage());
				}
			} else {
				try {
					preferences.load(defaultPrefs);
					log.info("Default preferences loaded");
				} catch (Exception e) {
					log.error("Error loading default preferences: {}", e.getMessage());
				}
			}
		} catch (Exception e) {
			log.error("Failed to load preferences: {}", e.getMessage());
		}
	}
	
	public void savePreferences() {
		// TODO Trigger upon save button click
		// TODO Trigger on end for target directory update
		try {
			OutputStream out = Files.newOutputStream(prefPath);
			preferences.store(out, "ReactionSorter Preferences");
		} catch (IOException e) {
			log.error("Error saving preferences: {}", e.getMessage());
		}
	}
}
