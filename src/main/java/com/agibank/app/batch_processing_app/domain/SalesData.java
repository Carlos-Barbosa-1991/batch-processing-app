package com.agibank.app.batch_processing_app.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.NumberFormat;

public class SalesData {
	
	private String idData;
	
	private String idSale;
	
	private List<SalesItems> salesItems;
	
	private String nameSalesman;
	
	@NumberFormat(pattern="#0.00")
	private BigDecimal saleValue;

	public SalesData() {
		this.saleValue = BigDecimal.ZERO;
		this.salesItems = new ArrayList<>();
	};
	
	public String getIdData() {
		return idData;
	}

	public void setIdData(String idData) {
		this.idData = idData;
	}

	public String getIdSale() {
		return idSale;
	}

	public void setIdSale(String idSale) {
		this.idSale = idSale;
	}

	public List<SalesItems> getSalesItems() {
		return salesItems;
	}

	public void setSalesItems(List<SalesItems> salesItems) {
		this.salesItems = salesItems;
	}

	public String getNameSalesman() {
		return nameSalesman;
	}

	public void setNameSalesman(String nameSalesman) {
		this.nameSalesman = nameSalesman;
	}

	public BigDecimal getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(BigDecimal saleValue) {
		this.saleValue = saleValue;
	}

}
