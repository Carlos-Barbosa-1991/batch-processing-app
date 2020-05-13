package com.agibank.app.batch.processing.domain;

public class ClientData {

  private String idData;

  private String cnpj;

  private String name;

  private String businessArea;

  public String getIdData() {
    return idData;
  }

  public void setIdData(String idData) {
    this.idData = idData;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBusinessArea() {
    return businessArea;
  }

  public void setBusinessArea(String businessArea) {
    this.businessArea = businessArea;
  }

}
