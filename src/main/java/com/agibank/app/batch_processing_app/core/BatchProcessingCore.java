package com.agibank.app.batch_processing_app.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.agibank.app.batch_processing_app.domain.ClientData;
import com.agibank.app.batch_processing_app.domain.ConsolidatedData;
import com.agibank.app.batch_processing_app.domain.LayoutFile;
import com.agibank.app.batch_processing_app.domain.SalesData;
import com.agibank.app.batch_processing_app.domain.SalesItems;
import com.agibank.app.batch_processing_app.domain.SellerData;

public class BatchProcessingCore {
	
	private String idSaleExpensive;
	private Long countSellers;
	private Long countClient;
	private String nameWorstSeller;
	File fileLocation = new File(System.getProperty("user.home"),"\\data\\in\\");
	File reportLocation = new File(System.getProperty("user.home"),"\\data\\out\\");
	LayoutFile layout = new LayoutFile();
	
	public BatchProcessingCore(){

	};
	
	public Boolean generateReport(ConsolidatedData consolidatedData, String fileName) throws Exception {
		
		List<SellerData> sellerData = consolidatedData.getSellerData().stream().filter(seller -> !seller.getCpf().isEmpty())
																			   .filter(distinctByKey(seller -> seller.getCpf()))
																			   .collect(Collectors.toList());
																			   
		
		List<ClientData> clientData = consolidatedData.getClientData().stream().filter(client -> !client.getCnpj().isEmpty())
																			   .filter(distinctByKey(client -> client.getCnpj()))
																			   .collect(Collectors.toList());
																			   
		
		List<SalesData> salesData = consolidatedData.getSalesData().stream().filter(sales -> !sales.getSalesItems().isEmpty()).collect(Collectors.toList());
		
		//Quantidade de vendedores no arquivo
		countSellers = sellerData.stream().count();
		
		//Quantidade de clientes no arquivo
		countClient = clientData.stream().count();
		
		
		try {
			//Consolida vendas por vendedor			
			if(this.consolidatedSalesBySeller(sellerData, salesData) == false) {
				LayoutFile layout = new LayoutFile();
				this.exportReport(fileName, layout);
				return false;
			};
			
		} catch (IOException e) {
			System.err.printf("Erro ao consolidar vendas por vendedor");
			e.printStackTrace();
			return false;
		}
		
		//Busca pior vendedor
		nameWorstSeller = salesData.stream().min(Comparator.comparing(SalesData::getSaleValue))
										    .orElseThrow(NoSuchElementException::new)
				                            .getNameSalesman();
		
		//Busca o Id da maior venda	
		idSaleExpensive = salesData.stream().max(Comparator.comparing(SalesData::getSaleValue))
				   							.orElseThrow(NoSuchElementException::new)
				   							.getIdSale();
			
		System.out.println("Arquivo: ["+ fileName +"] processado.");
		
		layout = this.populateLayout(idSaleExpensive, countSellers, countClient, nameWorstSeller );
		
		this.exportReport(fileName, layout);
		
		return true;
		
	};
	
	public ConsolidatedData transform(String line,ConsolidatedData consolidatedData) {
		
		try {
			
			String[] data = line.split("ç");
			
			if(data[0].equals("001") && data[0] != null) {
				SellerData sellerData = new SellerData();

				sellerData.setIdData(data[0]);
				sellerData.setCpf(data[1]);
				sellerData.setName(data[2]);
				sellerData.setSalary(new BigDecimal(data[3]));
				consolidatedData.getSellerData().add(sellerData);

			}
			else if(data[0].equals("002") && data[0] != null) {
				ClientData clientData = new ClientData();

				clientData.setIdData(data[0]);
				clientData.setCnpj(data[1]);
				clientData.setName(data[2]);
				clientData.setBusinessArea(data[3]);
				consolidatedData.getClientData().add(clientData);

			} 
			else if(data[0].equals("003") && data[0] != null) {
				SalesData salesData = new SalesData();

				salesData.setIdData(data[0]);
				salesData.setIdSale(data[1]);
				salesData.setNameSalesman(data[3]);
				salesData.getSalesItems().addAll(transformListItems(data[2]));
				consolidatedData.getSalesData().add(salesData);
			}else{
				System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
				return null;
			};

			return consolidatedData;
		} catch(Exception e) {
			System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
			return null;
		}
		
	};
	
	public List<SalesItems> transformListItems(String items) {

		try {
			List<SalesItems> salesListItems = new ArrayList<SalesItems>();

			int firstIndex = items.indexOf("[");
			int lastIndex = items.lastIndexOf("]");
			String[] firstSplitItems = items.substring(firstIndex + 1, lastIndex).split(",");


			for (String splitItems : firstSplitItems) {
				String[] secondSplitItems = splitItems.split("-");

				SalesItems salesItems = new SalesItems();

				salesItems.setIdItem(secondSplitItems[0]);
				salesItems.setItemQuantity(Long.valueOf(secondSplitItems[1]));
				salesItems.setItemPrice(new BigDecimal(secondSplitItems[2]));
				salesListItems.add(salesItems);
			}

			return salesListItems;
		}catch(Exception e) {
			e.printStackTrace();
			return null;

		}

	};	
	
	public Boolean consolidatedSalesBySeller(List<SellerData> sellerData, List<SalesData> salesData) throws Exception {
		
		if(sellerData.isEmpty() || salesData.isEmpty()) {
			System.out.println("Não há vendas a serem processadas");
			return false;
		};
		
		sellerData.forEach(seller ->{ 
		
			List<SalesData> salesByName = new ArrayList<SalesData>();
			 
			if(seller.getName() != null) {
				 salesByName = salesData.stream().filter(sD -> !sD.getNameSalesman().isEmpty())
						 						 .filter(sD -> sD.getNameSalesman().equals(seller.getName()))
						 						 .collect(Collectors.toList());
			};
			
			salesByName.forEach(sale -> {
							
				sale.getSalesItems().forEach(items -> {
							
					if(items != null) {
						SalesData saleData = new SalesData();
						BigDecimal sumValue = sale.getSaleValue();
						
						saleData.setSaleValue(items.getItemPrice().multiply(BigDecimal.valueOf(items.getItemQuantity())));
						sumValue = saleData.getSaleValue().add(sumValue);
						sale.setSaleValue(sumValue);
					};	
					
				});
			
			});
			
		});
		
		return true;
		
	};
	
	public void startProcessing() {
		
    	try {
    		
    		File[] filesIn = this.repositoryListener();
    	
    		int initialQueue = filesIn.length - 1;
    		int finalQueue = filesIn.length + 1;
    		
    		do {

    			if(filesIn.length > initialQueue) {
    				filesIn = this.repositoryListener();
    				initialQueue = filesIn.length - 1;
    				finalQueue = filesIn.length + 1;

    				for (int i = 0; i < filesIn.length; i++) {
    					
    					if(!this.validateFileExistence(filesIn[i].getName())) {
    						
    						System.out.println("\nInício do processamento do arquivo: ["+ filesIn[i].getName()+"]");
    						long start = System.currentTimeMillis();

    						Path path = Paths.get(filesIn[i].getPath());
    						ConsolidatedData consolidatedData = new ConsolidatedData();

    						List<String> linesFile = Files.readAllLines(path);

    						for (String line : linesFile) {
    							consolidatedData = this.transform(line, consolidatedData);
    							
    							if(consolidatedData == null) {
    								continue;
    							}

    						}   

    						try {
    							Boolean statusGenerateReport = this.generateReport(consolidatedData, filesIn[i].getName());

    							long elapsed = (System.currentTimeMillis() - start);
    							System.out.println("Arquivo: ["+ filesIn[i].getName()+ "]"+ " Tempo de processamento em milissegundos: [" + elapsed +"ms ]");

    							if(!statusGenerateReport) {
    								continue;
    							}
    							
    						} catch (Exception e) {
    							LayoutFile layout = new LayoutFile();
    							this.exportReport(filesIn[i].getName(), layout);
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
	
	public String removeExtentionFileName(String file) {

		int lastIndex = file.lastIndexOf(".");
		String fileNameFitting = file.substring(0, lastIndex);
		
		return fileNameFitting;

	};
	
	public void exportReport(String fileName, LayoutFile layout) {
		
		if(!reportLocation.exists()) {
			reportLocation.mkdir();
		};
		
		String properFileName = this.removeExtentionFileName(fileName);
		
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
	
	public LayoutFile populateLayout(String idSaleExpensive, Long countSellers, Long countClient, String nameWorstSeller ) {

		LayoutFile layoutFile = new LayoutFile();

		layoutFile.setIdSaleExpensive(idSaleExpensive);
		layoutFile.setCountSellers(countSellers);
		layoutFile.setCountClient(countClient);
		layoutFile.setNameWorstSeller(nameWorstSeller);

		return layoutFile;

	};
	
	public Boolean validateFileExistence(String fileName) {
		
		String properFileName = this.removeExtentionFileName(fileName);
		
		File file = new File(reportLocation,"" + properFileName + ".done.dat");
		
		if(file.exists()) {
			return true;
		} else {
			return false;
		}
		
	};
	
	public File[] repositoryListener() throws IOException, InterruptedException {
		
		//Delay devido a demora do sistema para escrever o arquivo na pasta de entrada
		Thread.currentThread().sleep(2000);
		
		File[] fileIn = fileLocation.listFiles(
				new FileFilter() { 
					public boolean accept(File b){
						return b.getName().endsWith(".dat");}
				}
			);
		
		return fileIn;
		
	};
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		   
		    Map<Object, Boolean> seen = new ConcurrentHashMap<>(); 
		    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null; 
		}
	
	

}
