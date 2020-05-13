package com.agibank.app.batch.processing.reports;

import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.utils.FileUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReportDefault {

  private final FileUtils fileUtils = new FileUtils();

  public void exportDefaultReport(String fileName, LayoutFile layout, File reportLocation) {

    if (!reportLocation.exists()) {
      reportLocation.mkdir();
    }

    String properFileName = fileUtils.removeExtentionFileName(fileName);

    File createFile = new File(reportLocation, "" + properFileName + ".done.dat");

    try {
      if (!createFile.exists()) {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(createFile));
        buffWrite.append("| Quantidade de clientes: ")
            .append(String.valueOf(layout.getCountClient()))
            .append(" | Quantidade de Vendedores: ")
            .append(String.valueOf(layout.getCountSellers()))
            .append(" | ID da Venda Mais Cara: ")
            .append(layout.getIdSaleExpensive())
            .append(" | Pior Vendedor: ")
            .append(layout.getNameWorstSeller())
            .append(" |\n");

        buffWrite.close();

        System.out.print("Relatório Exportado com sucesso, caminho: [" + createFile + "]\n");
      }

    } catch (IOException e) {
      System.err.print("Erro ao exportar relatório: [" + createFile + "]");
      e.printStackTrace();
    }

  }

}
