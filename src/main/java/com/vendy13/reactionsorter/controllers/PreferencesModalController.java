package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.utils.PreferencesManager;
import com.vendy13.reactionsorter.utils.DirectoryFormatter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class PreferencesModalController implements StageAwareController {
	@FXML
	private TextField defaultWorkingDisplay;
	@FXML
	private TextField defaultTargetDisplay;
	@FXML
	private Button defaultWorkingChoose;
	@FXML
	private Button defaultTargetChoose;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	@FXML
	private CheckBox confirmMove;
	@FXML
	private CheckBox autoplay;
	@FXML
	private CheckBox persistentVolume;
	@FXML
	private Slider defaultVolume;
	
	private final PreferencesManager preferencesManager;
	
	private String defaultWorkingDirectory;
	private String defaultTargetDirectory;
	private Stage stage;
	
	@Autowired
	public PreferencesModalController(PreferencesManager preferencesManager) {
		this.preferencesManager = preferencesManager;
		defaultWorkingDirectory = preferencesManager.getPreference("defaultWorkingDirectory");
		defaultTargetDirectory = preferencesManager.getPreference("defaultTargetDirectory");
	}
	
	@FXML
	public void init() {
		stage = (Stage) saveButton.getScene().getWindow();
		
		// TODO Tooltip for full paths
		// Load preferences into modal
		defaultWorkingDisplay.setText(DirectoryFormatter.shortenDirectory(defaultWorkingDirectory));
		defaultTargetDisplay.setText(DirectoryFormatter.shortenDirectory(defaultTargetDirectory));
		confirmMove.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("confirmMove")));
		autoplay.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("autoplay")));
		persistentVolume.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("persistentVolume")));
		defaultVolume.setValue(Double.parseDouble(preferencesManager.getPreference("defaultVolume")));
		
		defaultWorkingChoose.setOnAction(event -> browseDirectories(false));
		defaultTargetChoose.setOnAction(event -> browseDirectories(true));
		saveButton.setOnAction(event -> save());
		cancelButton.setOnAction(event -> stage.close());
	}
	
	// TODO make available for starting & working scenes
	private void browseDirectories(boolean isTarget) {
		String directory = isTarget ? defaultTargetDirectory : defaultWorkingDirectory;
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setInitialDirectory(new File(directory));
		dirChooser.setTitle("Select Default " + (isTarget ? "Target" : "Working") + " Directory");
		File selectedDir = dirChooser.showDialog(stage);
		
		if (selectedDir != null) {
			directory = selectedDir.getAbsolutePath();
			
			if (isTarget) {
				defaultTargetDirectory = directory;
			} else {
				defaultWorkingDirectory = directory;
			}
		}
		
		(isTarget ? defaultTargetDisplay : defaultWorkingDisplay).setText(DirectoryFormatter.shortenDirectory(directory));
	}
	
	private void save() {
		preferencesManager.setPreference("defaultWorkingDirectory", defaultWorkingDisplay.getText());
		preferencesManager.setPreference("defaultTargetDirectory", defaultTargetDisplay.getText());
		preferencesManager.setPreference("confirmMove", String.valueOf(confirmMove.isSelected()));
		preferencesManager.setPreference("autoplay", String.valueOf(autoplay.isSelected()));
		preferencesManager.setPreference("persistentVolume", String.valueOf(persistentVolume.isSelected()));
		preferencesManager.setPreference("defaultVolume", String.valueOf((int) defaultVolume.getValue()));
		preferencesManager.savePreferences();
		
		stage.close();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
