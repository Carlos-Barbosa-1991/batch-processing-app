package com.agibank.app.batch.processing.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SalesItems;
import com.agibank.app.batch.processing.domain.SellerData;

public class ConverterUtils {
	
	public ConsolidatedData transformLines(String line,ConsolidatedData consolidatedData) {
		
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

}
