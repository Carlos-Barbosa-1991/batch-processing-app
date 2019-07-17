package com.agibank.app.batch.processing.validate;

import java.io.File;

import com.agibank.app.batch.processing.utils.FileUtils;

public class FileValidate {

	private FileUtils fileUtils = new FileUtils();

	public FileValidate() {

	}

	public Boolean validateFileExistence(String fileName, File reportLocation) {

		String properFileName = fileUtils.removeExtentionFileName(fileName);

		File file = new File(reportLocation, "" + properFileName + ".done.dat");

		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	};

}
