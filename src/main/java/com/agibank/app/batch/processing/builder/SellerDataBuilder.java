package com.agibank.app.batch.processing.builder;

import com.agibank.app.batch.processing.domain.SellerData;
import java.math.BigDecimal;

public class SellerDataBuilder {

  private final SellerData sellerTmp;

  public SellerDataBuilder() {
    sellerTmp = new SellerData();
  }

  public SellerData build() {

    SellerData seller = new SellerData();

    seller.setIdData(sellerTmp.getIdData());
    seller.setCpf(sellerTmp.getCpf());
    seller.setName(sellerTmp.getName());
    seller.setSalary(sellerTmp.getSalary());

    return seller;
  }

  public SellerDataBuilder getSellerData(String[] data) {

    sellerTmp.setIdData(data[0]);
    sellerTmp.setCpf(data[1]);
    sellerTmp.setName(data[2]);
    sellerTmp.setSalary(new BigDecimal(data[3]));

    return this;
  }

}
