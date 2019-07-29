package com.agibank.app.batch.processing.factories;

import com.agibank.app.batch.processing.builder.ClientDataBuilder;
import com.agibank.app.batch.processing.builder.SalesDataBuilder;
import com.agibank.app.batch.processing.builder.SellerDataBuilder;
import com.agibank.app.batch.processing.domain.ConsolidatedData;

public class ConsolidatedDataFactory {

	private static SellerDataBuilder sellerBuilder = new SellerDataBuilder();
	private static ClientDataBuilder clientBuilder = new ClientDataBuilder();
	private static SalesDataBuilder salesBuilder = new SalesDataBuilder();

	public static ConsolidatedData getConsolidatedData(String line, ConsolidatedData consolidatedData) {

		try {
			
			String[] data = line.split("ç");

			if (data[0].equals("001")) {

				consolidatedData.getSellerData().add(sellerBuilder.getSellerData(data).build());

			} else if (data[0].equals("002")) {

				consolidatedData.getClientData().add(clientBuilder.getClientData(data).build());

			} else if (data[0].equals("003")) {
				
				consolidatedData.getSalesData().add(salesBuilder.getSalesData(data).build());

			} else {
				System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
				
			}
			
			return consolidatedData;

		} catch (Exception e) {
			System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
			return consolidatedData;
		}

	}

}
