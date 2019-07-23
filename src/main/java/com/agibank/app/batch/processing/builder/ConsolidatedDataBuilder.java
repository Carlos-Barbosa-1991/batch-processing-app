package com.agibank.app.batch.processing.builder;

import com.agibank.app.batch.processing.domain.ConsolidatedData;

public class ConsolidatedDataBuilder {

	private SellerDataBuilder sellerBuilder = new SellerDataBuilder();
	private ClientDataBuilder clientBuilder = new ClientDataBuilder();
	private SalesDataBuilder salesBuilder = new SalesDataBuilder();
	private ConsolidatedData consolidatedTmp; 

	public ConsolidatedDataBuilder() {
		consolidatedTmp = new ConsolidatedData();

	};
	
	public ConsolidatedData build() {

		ConsolidatedData consolidated = new ConsolidatedData();

		consolidated.setSellerData(consolidatedTmp.getSellerData());
		consolidated.setClientData(consolidatedTmp.getClientData());
		consolidated.setSalesData(consolidatedTmp.getSalesData());

		return consolidated;
	}

	public ConsolidatedDataBuilder getConsolidatedData(String line, ConsolidatedData consolidatedData) {

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
			
			consolidatedTmp = consolidatedData;

			return this;

		} catch (Exception e) {
			System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
			return this;
		}

	}

}
