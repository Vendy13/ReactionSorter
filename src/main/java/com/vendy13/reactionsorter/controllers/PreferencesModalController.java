package com.vendy13.reactionsorter.controllers;

import com.vendy13.reactionsorter.utils.DirectoryUtils;
import com.vendy13.reactionsorter.services.PreferencesService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private CheckBox loop;
	@FXML
	private CheckBox persistentVolume;
	@FXML
	private Slider defaultVolumeSlider;
	@FXML
	private Tooltip defaultWorkingTooltip;
	@FXML
	private Tooltip defaultTargetTooltip;
	
	private static final Logger log = LoggerFactory.getLogger(PreferencesModalController.class);
	
	private final PreferencesService preferencesService;
	
	private Stage stage;
	
	@Autowired
	public PreferencesModalController(PreferencesService preferencesService) {
		this.preferencesService = preferencesService;
	}
	
	@FXML
	public void init() {
		stage = (Stage) saveButton.getScene().getWindow();
		
		String defaultWorkingDirectory = preferencesService.getPreference("defaultWorkingDirectory");
		String defaultTargetDirectory = preferencesService.getPreference("defaultTargetDirectory");
		
		// Load preferences into modal
		defaultWorkingDisplay.setText(DirectoryUtils.shortenDirectory(defaultWorkingDirectory));
		defaultTargetDisplay.setText(DirectoryUtils.shortenDirectory(defaultTargetDirectory));
		defaultWorkingTooltip.setText(defaultWorkingDirectory);
		defaultTargetTooltip.setText(defaultTargetDirectory);
		confirmMove.setSelected(Boolean.parseBoolean(preferencesService.getPreference("confirmMove")));
		autoplay.setSelected(Boolean.parseBoolean(preferencesService.getPreference("autoplay")));
		loop.setSelected(Boolean.parseBoolean(preferencesService.getPreference("loop")));
		persistentVolume.setSelected(Boolean.parseBoolean(preferencesService.getPreference("persistentVolume")));
		defaultVolumeSlider.setValue(Double.parseDouble(preferencesService.getPreference("defaultVolume")));
		defaultVolume.setText(preferencesService.getPreference("defaultVolume"));
		
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
		try {
			preferencesService.setPreference("defaultWorkingDirectory", defaultWorkingTooltip.getText());
			preferencesService.setPreference("defaultTargetDirectory", defaultTargetTooltip.getText());
			preferencesService.setPreference("confirmMove", String.valueOf(confirmMove.isSelected()));
			preferencesService.setPreference("autoplay", String.valueOf(autoplay.isSelected()));
			preferencesService.setPreference("loop", String.valueOf(loop.isSelected()));
			preferencesService.setPreference("persistentVolume", String.valueOf(persistentVolume.isSelected()));
			preferencesService.setPreference("defaultVolume", String.valueOf((int) defaultVolumeSlider.getValue()));
			preferencesService.savePreferences();
			
			log.info("Preferences successfully saved to: {}", preferencesService.getPrefPath());
		} catch (Exception e) {
			log.error("Error saving preferences: {}", e.getMessage());
		}
		
		stage.close();
	}
	
	@Override
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
