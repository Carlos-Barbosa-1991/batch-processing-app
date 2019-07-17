package com.agibank.app.batch.processing.builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SalesItems;

public class SalesDataBuilder {

	private SalesData salesTmp;

	public SalesDataBuilder() {
		salesTmp = new SalesData();

	};

	public SalesData build() {

		SalesData sales = new SalesData();

		sales.setIdData(salesTmp.getIdData());
		sales.setIdSale(salesTmp.getIdSale());
		sales.setSalesItems(salesTmp.getSalesItems());
		sales.setNameSalesman(salesTmp.getNameSalesman());

		return sales;

	}

	public SalesDataBuilder getSalesData(String[] data) {

		salesTmp.setIdData(data[0]);
		salesTmp.setIdSale(data[1]);
		salesTmp.setSalesItems(transformListItems(data[2]));
		salesTmp.setNameSalesman(data[3]);

		return this;
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
		} catch (Exception e) {
			System.err.print("Arquivo fora do layout. Linha: [" + items + "]\n");
			e.printStackTrace();
			return null;

		}

	}

}
