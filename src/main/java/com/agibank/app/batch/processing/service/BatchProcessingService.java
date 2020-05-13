package com.agibank.app.batch.processing.service;

import com.agibank.app.batch.processing.core.BatchProcessingCore;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.factories.ConsolidatedDataFactory;
import com.agibank.app.batch.processing.reports.ReportDefault;
import com.agibank.app.batch.processing.validate.FileValidate;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class BatchProcessingService {

  private final File fileLocation = new File(System.getProperty("user.home"), "\\data\\in\\");
  private final File reportLocation = new File(System.getProperty("user.home"), "\\data\\out\\");
  private final FileValidate fileValidate = new FileValidate();
  private final BatchProcessingCore batchCore = new BatchProcessingCore();
  private final ReportDefault reportModels = new ReportDefault();

  public void startProcessing() {

    try {

      File[] filesIn = this.repositoryListener();

      int initialQueue = filesIn.length - 1;
      int finalQueue = filesIn.length + 1;

      do {

        if (filesIn.length > initialQueue) {
          filesIn = this.repositoryListener();
          initialQueue = filesIn.length - 1;
          finalQueue = filesIn.length + 1;

          for (File file : filesIn) {

            if (!fileValidate.validateFileExistence(file.getName(), reportLocation)) {

              System.out
                  .println("\nInício do processamento do arquivo: [" + file.getName() + "]");

              long start = System.currentTimeMillis();

              Path path = Paths.get(file.getPath());

              List<String> linesFile = Files.readAllLines(path);

              ConsolidatedData consolidatedData = ConsolidatedDataFactory
                  .getConsolidatedData(linesFile);

              try {
                batchCore.generateReport(consolidatedData, file.getName(), reportLocation);

                long elapsed = (System.currentTimeMillis() - start);

                System.out.println("Arquivo: [" + file.getName() + "]"
                    + " Tempo de processamento em milissegundos: [" + elapsed + "ms ]");

              } catch (Exception e) {
                LayoutFile layout = new LayoutFile();
                reportModels.exportDefaultReport(file.getName(), layout, reportLocation);
                e.printStackTrace();
              }
            }
          }
        }

      } while (filesIn.length < finalQueue);

    } catch (Exception e) {
      System.err.print("Erro ao processar arquivos\n");
      e.printStackTrace();
    }

  }

  public File[] repositoryListener() throws Exception {

    // Delay devido a demora do sistema para escrever o arquivo na pasta de entrada
    Thread.sleep(2000);

    return fileLocation.listFiles(b -> b.getName().endsWith(".dat"));

  }

}
