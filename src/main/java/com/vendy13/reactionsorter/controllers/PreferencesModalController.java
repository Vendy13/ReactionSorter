package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.utils.DirectoryUtils;
import com.vendy13.reactionsorter.utils.PreferencesManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreferencesModalController implements StageAwareController {
	@FXML
	private Text defaultVolume;
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
	private Slider defaultVolumeSlider;
	@FXML
	private Tooltip defaultWorkingTooltip;
	@FXML
	private Tooltip defaultTargetTooltip;
	
	private final PreferencesManager preferencesManager;
	
	private Stage stage;
	
	@Autowired
	public PreferencesModalController(PreferencesManager preferencesManager) {
		this.preferencesManager = preferencesManager;
	}
	
	@FXML
	public void init() {
		stage = (Stage) saveButton.getScene().getWindow();
		
		String defaultWorkingDirectory = preferencesManager.getPreference("defaultWorkingDirectory");
		String defaultTargetDirectory = preferencesManager.getPreference("defaultTargetDirectory");
		
		// Load preferences into modal
		defaultWorkingDisplay.setText(DirectoryUtils.shortenDirectory(defaultWorkingDirectory));
		defaultTargetDisplay.setText(DirectoryUtils.shortenDirectory(defaultTargetDirectory));
		defaultWorkingTooltip.setText(defaultWorkingDirectory);
		defaultTargetTooltip.setText(defaultTargetDirectory);
		confirmMove.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("confirmMove")));
		autoplay.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("autoplay")));
		persistentVolume.setSelected(Boolean.parseBoolean(preferencesManager.getPreference("persistentVolume")));
		defaultVolumeSlider.setValue(Double.parseDouble(preferencesManager.getPreference("defaultVolume")));
		defaultVolume.setText(preferencesManager.getPreference("defaultVolume"));
		
		saveButton.setOnAction(event -> save());
		cancelButton.setOnAction(event -> stage.close());
		
		defaultWorkingChoose.setOnAction(event ->
				DirectoryUtils.chooseDirectories(true, false, defaultWorkingDisplay,defaultWorkingTooltip,
						stage));
		defaultTargetChoose.setOnAction(event ->
				DirectoryUtils.chooseDirectories(true, true, defaultTargetDisplay, defaultTargetTooltip,
						stage));
		
		defaultVolumeSlider.valueProperty().addListener((observable, oldValue, newValue) ->
				defaultVolume.setText(String.valueOf((int) defaultVolumeSlider.getValue())));
	}
	
	private void save() {
		preferencesManager.setPreference("defaultWorkingDirectory", defaultWorkingTooltip.getText());
		preferencesManager.setPreference("defaultTargetDirectory", defaultTargetTooltip.getText());
		preferencesManager.setPreference("confirmMove", String.valueOf(confirmMove.isSelected()));
		preferencesManager.setPreference("autoplay", String.valueOf(autoplay.isSelected()));
		preferencesManager.setPreference("persistentVolume", String.valueOf(persistentVolume.isSelected()));
		preferencesManager.setPreference("defaultVolume", String.valueOf((int) defaultVolumeSlider.getValue()));
		preferencesManager.savePreferences();
		
		stage.close();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
