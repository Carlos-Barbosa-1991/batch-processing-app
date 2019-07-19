package com.agibank.app.batch.processing.builder;

import java.util.ArrayList;
import java.util.List;

import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SellerData;

public class ConsolidatedDataBuilder {

	private SellerDataBuilder sellerBuilder = new SellerDataBuilder();
	private ClientDataBuilder clientBuilder = new ClientDataBuilder();
	private SalesDataBuilder salesBuilder = new SalesDataBuilder();

	public ConsolidatedDataBuilder() {

	};

	public ConsolidatedData constructObjects(String line, ConsolidatedData consolidatedData) {

		try {

			String[] data = line.split("ç");

			if (data[0].equals("001")) {

				List<SellerData> listSellerData = new ArrayList<SellerData>();

				listSellerData.add(sellerBuilder.getSellerData(data).build());
				consolidatedData.getSellerData().addAll(listSellerData);

			} else if (data[0].equals("002")) {

				List<ClientData> listClientData = new ArrayList<ClientData>();

				listClientData.add(clientBuilder.getClientData(data).build());
				consolidatedData.getClientData().addAll(listClientData);

			} else if (data[0].equals("003")) {
				
				List<SalesData> listSalesData = new ArrayList<SalesData>();

				listSalesData.add(salesBuilder.getSalesData(data).build());
				consolidatedData.getSalesData().addAll(listSalesData);

			} else {
				System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
				return null;
			}

			return consolidatedData;

		} catch (Exception e) {
			System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
			return null;
		}

	}

}
