package com.agibank.app.batch.processing.reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.utils.FileUtils;

public class ReportModels {
	
	private FileUtils fileUtils = new FileUtils();
	
	public void exportDefaultReport(String fileName, LayoutFile layout, File reportLocation) {
		
		if(!reportLocation.exists()) {
			reportLocation.mkdir();
		};
		
		String properFileName = fileUtils.removeExtentionFileName(fileName);
		
		File createFile = new File(reportLocation,"" + properFileName + ".done.dat");
		
		try {
			if(!createFile.exists()) {
				BufferedWriter buffWrite = new BufferedWriter(new FileWriter(createFile));
				buffWrite.append("| Quantidade de clientes: " + String.valueOf(layout.getCountClient() + " | "))
						 .append("Quantidade de Vendedores: " + String.valueOf(layout.getCountSellers() + " | "))
						 .append("ID da Venda Mais Cara: " + layout.getIdSaleExpensive() + " | ")
						 .append("Pior Vendedor: " + layout.getNameWorstSeller() +" |\n");

				buffWrite.close();
				
				System.out.printf("Relatório Exportado com sucesso, caminho: [" + createFile + "]\n");
			}
			
		} catch (IOException e) {
			System.err.printf("Erro ao exportar relatório: ["+ createFile +"]");
			e.printStackTrace();
		}
		
	};

}
