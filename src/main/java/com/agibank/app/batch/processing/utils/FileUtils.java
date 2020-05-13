package com.agibank.app.batch.processing.utils;

public class FileUtils {

  public String removeExtentionFileName(String file) {
    return file.substring(0, file.lastIndexOf("."));

  }

}
