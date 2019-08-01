package com.agibank.app.batch.processing.factories;

import java.util.List;

import com.agibank.app.batch.processing.builder.ClientDataBuilder;
import com.agibank.app.batch.processing.builder.SalesDataBuilder;
import com.agibank.app.batch.processing.builder.SellerDataBuilder;
import com.agibank.app.batch.processing.domain.ConsolidatedData;

public class ConsolidatedDataFactory {

	private static SellerDataBuilder sellerBuilder = new SellerDataBuilder();
	private static ClientDataBuilder clientBuilder = new ClientDataBuilder();
	private static SalesDataBuilder salesBuilder = new SalesDataBuilder();

	public static ConsolidatedData getConsolidatedData(List<String> linesFile) {

		ConsolidatedData consolidatedData = new ConsolidatedData();

		for (String line : linesFile) {

			try {

				String[] data = line.split("ç");

				switch (data[0]) {
				case "001":
					consolidatedData.getSellerData().add(sellerBuilder.getSellerData(data).build());
					break;
				case "002":
					consolidatedData.getClientData().add(clientBuilder.getClientData(data).build());
					break;
				case "003":
					consolidatedData.getSalesData().add(salesBuilder.getSalesData(data).build());
					break;
				default:
					System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
				}

			} catch (Exception e) {
				System.err.print("Arquivo fora do layout. Linha: [" + line + "]\n");
				return consolidatedData;
			}

		}

		return consolidatedData;

	}

}
