package com.agibank.app.batch.processing.domain;

import java.util.ArrayList;
import java.util.List;

public class ConsolidatedData {
	
	private List<SellerData> sellerData;
	
	private List<ClientData> clientData;
	
	private List<SalesData> salesData;
	
	public ConsolidatedData() {
		this.sellerData = new ArrayList<>();
		this.clientData = new ArrayList<>();
		this.salesData = new ArrayList<>();
	};

	public List<SellerData> getSellerData() {
		return sellerData;
	}

	public void setSellerData(List<SellerData> sellerData) {
		this.sellerData = sellerData;
	}

	public List<ClientData> getClientData() {
		return clientData;
	}

	public void setClientData(List<ClientData> clientData) {
		this.clientData = clientData;
	}

	public List<SalesData> getSalesData() {
		return salesData;
	}

	public void setSalesData(List<SalesData> salesData) {
		this.salesData = salesData;
	}

}
