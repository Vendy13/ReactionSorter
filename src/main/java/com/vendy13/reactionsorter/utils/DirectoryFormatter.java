package com.vendy13.reactionsorter.utils;

public class DirectoryFormatter {
	private static final String SEPARATOR = System.getProperty("file.separator");
	
	public static String shortenDirectory(String directory) {
		return ".." + directory.substring(nthLastIndexOf(2, SEPARATOR, directory));
	}
	
	private static int nthLastIndexOf(int nth, String ch, String string) {
		if (nth <= 0) return string.length();
		return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
	}
}
