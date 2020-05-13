package com.agibank.app.batch.processing.validate;

import com.agibank.app.batch.processing.utils.FileUtils;
import java.io.File;

public class FileValidate {

  private final FileUtils fileUtils = new FileUtils();

  public FileValidate() {
  }

  public Boolean validateFileExistence(String fileName, File reportLocation) {

    String properFileName = fileUtils.removeExtentionFileName(fileName);

    File file = new File(reportLocation, "" + properFileName + ".done.dat");

    return file.exists();

  }

}
