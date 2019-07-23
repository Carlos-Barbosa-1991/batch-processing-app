package com.agibank.app.batch.processing.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.agibank.app.batch.processing.builder.ConsolidatedDataBuilder;
import com.agibank.app.batch.processing.core.BatchProcessingCore;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.reports.ReportDefault;
import com.agibank.app.batch.processing.validate.FileValidate;

public class BatchProcessingService {

	private File fileLocation = new File(System.getProperty("user.home"), "\\data\\in\\");
	private File reportLocation = new File(System.getProperty("user.home"), "\\data\\out\\");
	private FileValidate fileValidate = new FileValidate();
	private BatchProcessingCore batchCore = new BatchProcessingCore();
	private ReportDefault reportModels = new ReportDefault();
	private ConsolidatedDataBuilder consolidatedDataBuilder = new ConsolidatedDataBuilder();

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

					for (int i = 0; i < filesIn.length; i++) {

						if (!fileValidate.validateFileExistence(filesIn[i].getName(), reportLocation)) {

							System.out.println("\nInício do processamento do arquivo: [" + filesIn[i].getName() + "]");
							long start = System.currentTimeMillis();

							Path path = Paths.get(filesIn[i].getPath());
							ConsolidatedData consolidatedData = new ConsolidatedData();

							List<String> linesFile = Files.readAllLines(path);

							for (String line : linesFile) {
								consolidatedData = consolidatedDataBuilder.getConsolidatedData(line, consolidatedData)
										.build();

								if (consolidatedData.getClientData().isEmpty()
										|| consolidatedData.getSellerData().isEmpty()
										|| consolidatedData.getSalesData().isEmpty()) {
									continue;
								}

							}

							try {
								Boolean statusGenerateReport = batchCore.generateReport(consolidatedData,
										filesIn[i].getName(), reportLocation);

								long elapsed = (System.currentTimeMillis() - start);
								System.out.println("Arquivo: [" + filesIn[i].getName() + "]"
										+ " Tempo de processamento em milissegundos: [" + elapsed + "ms ]");

								if (!statusGenerateReport) {
									continue;
								}

							} catch (Exception e) {
								LayoutFile layout = new LayoutFile();
								reportModels.exportDefaultReport(filesIn[i].getName(), layout, reportLocation);
								e.printStackTrace();
								continue;
							}
						}
					}
				}

			} while (filesIn.length < finalQueue);

		} catch (Exception e) {
			System.err.printf("Erro ao processar arquivos\n");
			e.printStackTrace();
		}

	};

	public File[] repositoryListener() throws IOException, InterruptedException {

		// Delay devido a demora do sistema para escrever o arquivo na pasta de entrada
		Thread.sleep(2000);

		File[] fileIn = fileLocation.listFiles(new FileFilter() {
			public boolean accept(File b) {
				return b.getName().endsWith(".dat");
			}
		});

		return fileIn;

	};

}
