package com.agibank.app.batch.processing.domain;

public class LayoutFile {
	
	private String idSaleExpensive;
	private Long countSellers;
	private Long countClient;
	private String nameWorstSeller;
	
	public LayoutFile() {
		this.idSaleExpensive = "";
		this.countSellers = 0L;
		this.countClient = 0L;
		this.nameWorstSeller = "";
	};
	
	
	public String getIdSaleExpensive() {
		return idSaleExpensive;
	}
	public void setIdSaleExpensive(String idSaleExpensive) {
		this.idSaleExpensive = idSaleExpensive;
	}
	public Long getCountSellers() {
		return countSellers;
	}
	public void setCountSellers(Long countSellers) {
		this.countSellers = countSellers;
	}
	public Long getCountClient() {
		return countClient;
	}
	public void setCountClient(Long countClient) {
		this.countClient = countClient;
	}
	public String getNameWorstSeller() {
		return nameWorstSeller;
	}
	public void setNameWorstSeller(String nameWorstSeller) {
		this.nameWorstSeller = nameWorstSeller;
	}

}
