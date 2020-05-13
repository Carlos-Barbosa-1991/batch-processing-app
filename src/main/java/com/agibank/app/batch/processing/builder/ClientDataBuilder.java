package com.agibank.app.batch.processing.builder;

import com.agibank.app.batch.processing.domain.ClientData;

public class ClientDataBuilder {

  private final ClientData clientTmp;

  public ClientDataBuilder() {
    clientTmp = new ClientData();
  }

  public ClientData build() {

    ClientData client = new ClientData();

    client.setIdData(clientTmp.getIdData());
    client.setCnpj(clientTmp.getCnpj());
    client.setName(clientTmp.getName());
    client.setBusinessArea(clientTmp.getBusinessArea());

    return client;
  }

  public ClientDataBuilder getClientData(final String[] data) {

    clientTmp.setIdData(data[0]);
    clientTmp.setCnpj(data[1]);
    clientTmp.setName(data[2]);
    clientTmp.setBusinessArea(data[3]);

    return this;
  }

}
