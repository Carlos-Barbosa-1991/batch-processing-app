package com.agibank.app.batch_processing_app.domain;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

public class SellerData {
	
	private String idData;
	
	private String cpf;
	
	private String name;
	
	@NumberFormat(pattern="#0.00")
	private BigDecimal salary;

	public String getIdData() {
		return idData;
	}

	public void setIdData(String idData) {
		this.idData = idData;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}
	
}
