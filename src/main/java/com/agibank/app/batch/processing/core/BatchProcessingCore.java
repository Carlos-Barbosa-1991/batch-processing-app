package com.agibank.app.batch.processing.core;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SellerData;
import com.agibank.app.batch.processing.reports.ReportModels;
import com.agibank.app.batch.processing.utils.DistinctUtils;

public class BatchProcessingCore {
	
	private String idSaleExpensive;
	private Long countSellers;
	private Long countClient;
	private String nameWorstSeller;
	private LayoutFile layout = new LayoutFile();
	private DistinctUtils distinctUtils = new DistinctUtils();
	private ReportModels reportModels = new ReportModels();
	
	public BatchProcessingCore(){
		
	};
	
	public Boolean generateReport(ConsolidatedData consolidatedData, String fileName, File reportLocation) throws Exception {
		
		List<SellerData> sellerData = consolidatedData.getSellerData().stream().filter(seller -> !seller.getCpf().isEmpty())
																			   .filter(distinctUtils.distinctByKey(seller -> seller.getCpf()))
																			   .collect(Collectors.toList());
																			   
		
		List<ClientData> clientData = consolidatedData.getClientData().stream().filter(client -> !client.getCnpj().isEmpty())
																			   .filter(distinctUtils.distinctByKey(client -> client.getCnpj()))
																			   .collect(Collectors.toList());
																			   
		
		List<SalesData> salesData = consolidatedData.getSalesData().stream().filter(sales -> !sales.getSalesItems().isEmpty())
																			.collect(Collectors.toList());
		
		//Quantidade de vendedores no arquivo
		countSellers = sellerData.stream().count();
		
		//Quantidade de clientes no arquivo
		countClient = clientData.stream().count();
		
		
		try {
			//Consolida vendas por vendedor			
			if(this.consolidatedSalesBySeller(sellerData, salesData) == false) {
				LayoutFile layout = new LayoutFile();
				reportModels.exportDefaultReport(fileName, layout, reportLocation);
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
		
		reportModels.exportDefaultReport(fileName, layout, reportLocation);
		
		return true;
		
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
	
	public LayoutFile populateLayout(String idSaleExpensive, Long countSellers, Long countClient, String nameWorstSeller ) {

		LayoutFile layoutFile = new LayoutFile();

		layoutFile.setIdSaleExpensive(idSaleExpensive);
		layoutFile.setCountSellers(countSellers);
		layoutFile.setCountClient(countClient);
		layoutFile.setNameWorstSeller(nameWorstSeller);

		return layoutFile;

	};

}
