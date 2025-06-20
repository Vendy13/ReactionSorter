package com.vendy13.reactionsorter.configs;

import com.vendy13.reactionsorter.utils.PreferencesFileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class PreferencesConfig {
	@Autowired
	private PreferencesFileManager preferencesFileManager;
	
	private Path defaultWorkingDirectory;
	private Path defaultTargetDirectory;
	private boolean confirmMove;
	private boolean autoplay;
	private boolean persistentVolume;
	private int defaultVolume;
	
	public PreferencesConfig() {
		this.defaultWorkingDirectory = Path.of(PreferencesFileManager.USER_HOME, "Downloads");
		this.defaultTargetDirectory = Path.of(PreferencesFileManager.USER_HOME, "Documents");
		this.confirmMove = false;
		this.autoplay = false;
		this.persistentVolume = true;
		this.defaultVolume = 0;
	}
	
	public Path getDefaultWorkingDirectory() {
		return defaultWorkingDirectory;
	}
	
	public void setDefaultWorkingDirectory(Path defaultWorkingDirectory) {
		this.defaultWorkingDirectory = defaultWorkingDirectory;
	}
	
	public Path getDefaultTargetDirectory() {
		return defaultTargetDirectory;
	}
	
	public void setDefaultTargetDirectory(Path defaultTargetDirectory) {
		this.defaultTargetDirectory = defaultTargetDirectory;
	}
	
	public boolean isConfirmMove() {
		return confirmMove;
	}
	
	public void setConfirmMove(boolean confirmMove) {
		this.confirmMove = confirmMove;
	}
	
	public boolean isAutoplay() {
		return autoplay;
	}
	
	public void setAutoplay(boolean autoplay) {
		this.autoplay = autoplay;
	}
	
	public boolean isPersistentVolume() {
		return persistentVolume;
	}
	
	public void setPersistentVolume(boolean persistentVolume) {
		this.persistentVolume = persistentVolume;
	}
	
	public int getDefaultVolume() {
		return defaultVolume;
	}
	
	public void setDefaultVolume(int defaultVolume) {
		this.defaultVolume = defaultVolume;
	}
}
