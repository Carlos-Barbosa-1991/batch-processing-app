package com.agibank.app.batch.processing.utils;

public class FileUtils {
	
	public String removeExtentionFileName(String file) {

		int lastIndex = file.lastIndexOf(".");
		String fileNameFitting = file.substring(0, lastIndex);
		
		return fileNameFitting;

	};

}
