package com.agibank.app.batch.processing.domain;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

public class SalesItems {

	private String idItem;

	private Long itemQuantity;

	@NumberFormat(pattern = "#0.00")
	private BigDecimal itemPrice;

	public String getIdItem() {
		return idItem;
	}

	public void setIdItem(String idItem) {
		this.idItem = idItem;
	}

	public Long getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(Long itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

}
