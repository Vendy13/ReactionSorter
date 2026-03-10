package com.vendy13.reactionsorter.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.FileSystems;

public class DirectoryUtils {
	private static final String SEPARATOR = FileSystems.getDefault().getSeparator();
	
	public static String shortenDirectory(String directory) {
		return ".." + directory.substring(nthLastIndexOf(2, SEPARATOR, directory));
	}
	
	public static String chooseDirectories(boolean isPrefs, boolean isTarget, TextField display, Tooltip tooltip,
	                                       Stage stage) {
		String directory = tooltip.getText();
		
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setInitialDirectory(new File(directory));
		dirChooser.setTitle(isPrefs ? ("Select Default " + (isTarget ? "Target" : "Working") + " Directory") :
				("Select " + (isTarget ? "Target" : "Working") + " Directory"));
		File selectedDir = dirChooser.showDialog(stage);
		
		if (selectedDir != null) directory = selectedDir.getAbsolutePath();
		
		display.setText(DirectoryUtils.shortenDirectory(directory));
		tooltip.setText(directory);
		
		return directory;
	}
	
	private static int nthLastIndexOf(int nth, String ch, String string) {
		if (nth <= 0) return string.length();
		return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
	}
}
