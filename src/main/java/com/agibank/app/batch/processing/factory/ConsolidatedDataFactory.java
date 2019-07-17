package com.agibank.app.batch.processing.factory;

import java.util.ArrayList;
import java.util.List;

import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SellerData;
import com.agibank.app.batch.processing.builder.*;

public class ConsolidatedDataFactory {

	private SellerDataBuilder sellerBuilder = new SellerDataBuilder();
	private ClientDataBuilder clientBuilder = new ClientDataBuilder();
	private SalesDataBuilder salesBuilder = new SalesDataBuilder();

	public ConsolidatedDataFactory() {

	};

	public ConsolidatedData constructObjects(String line, ConsolidatedData consolidatedData) {

		try {

			String[] data = line.split("ç");

			if (data[0].equals("001")) {
				consolidatedData.getSellerData().addAll(this.getSellerData(data));

			} else if (data[0].equals("002")) {
				consolidatedData.getClientData().addAll(this.getClientData(data));

			} else if (data[0].equals("003")) {
				consolidatedData.getSalesData().addAll(this.getSalesData(data));

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

	public List<SellerData> getSellerData(String[] data) {

		List<SellerData> listSellerData = new ArrayList<SellerData>();

		listSellerData.add(sellerBuilder.getSellerData(data).build());

		return listSellerData;

	}

	public List<ClientData> getClientData(String[] data) {

		List<ClientData> listClientData = new ArrayList<ClientData>();

		listClientData.add(clientBuilder.getClientData(data).build());

		return listClientData;

	}

	public List<SalesData> getSalesData(String[] data) {

		List<SalesData> listSalesData = new ArrayList<SalesData>();

		listSalesData.add(salesBuilder.getSalesData(data).build());

		return listSalesData;

	}
}
