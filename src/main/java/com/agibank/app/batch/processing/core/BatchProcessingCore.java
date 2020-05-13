package com.agibank.app.batch.processing.core;

import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.LayoutFile;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SellerData;
import com.agibank.app.batch.processing.reports.ReportDefault;
import com.agibank.app.batch.processing.utils.DistinctUtils;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BatchProcessingCore {

  private final DistinctUtils distinctUtils = new DistinctUtils();
  private final ReportDefault reportModels = new ReportDefault();

  public BatchProcessingCore() {
  }

  public Boolean generateReport(ConsolidatedData consolidatedData, String fileName,
      File reportLocation) {

    //Dados dos vendedores
    List<SellerData> sellerData = consolidatedData.getSellerData().stream()
        .filter(seller -> !seller.getCpf().isEmpty())
        .filter(distinctUtils.distinctByKey(SellerData::getCpf))
        .collect(Collectors.toList());

    //Dados dos clientes
    List<ClientData> clientData = consolidatedData.getClientData().stream()
        .filter(client -> !client.getCnpj().isEmpty())
        .filter(distinctUtils.distinctByKey(ClientData::getCnpj))
        .collect(Collectors.toList());

    //Dados das vendas
    List<SalesData> salesData = consolidatedData.getSalesData().stream()
        .filter(sales -> !sales.getSalesItems().isEmpty()).collect(Collectors.toList());

    // Quantidade de vendedores no arquivo
    long countSellers = sellerData.size();

    // Quantidade de clientes no arquivo
    long countClient = clientData.size();

    // Consolida vendas por vendedor
    if (!this.consolidatedSalesBySeller(sellerData, salesData)) {
      LayoutFile layout = new LayoutFile();
      reportModels.exportDefaultReport(fileName, layout, reportLocation);
      return false;
    }

    // Busca pior vendedor
    String nameWorstSeller = salesData.stream().min(Comparator.comparing(SalesData::getSaleValue))
        .orElseThrow(NoSuchElementException::new).getNameSalesman();

    // Busca o Id da maior venda
    String idSaleExpensive = salesData.stream().max(Comparator.comparing(SalesData::getSaleValue))
        .orElseThrow(NoSuchElementException::new).getIdSale();

    System.out.println("Arquivo: [" + fileName + "] processado.");

    LayoutFile layout1 = this
        .populateLayout(idSaleExpensive, countSellers, countClient, nameWorstSeller);

    reportModels.exportDefaultReport(fileName, layout1, reportLocation);

    return true;

  }

  public Boolean consolidatedSalesBySeller(List<SellerData> sellerData, List<SalesData> salesData) {

    if (sellerData.isEmpty() || salesData.isEmpty()) {
      System.out.println("Não há vendas a serem processadas");
      return false;
    }

    sellerData.forEach(seller -> {

      List<SalesData> salesByName = new ArrayList<>();

      if (seller.getName() != null) {
        salesByName = salesData.stream().filter(sale -> !sale.getNameSalesman().isEmpty())
            .filter(sale -> sale.getNameSalesman().equals(seller.getName()))
            .collect(Collectors.toList());
      }

      salesByName.forEach(sale ->
          sale.getSalesItems().forEach(items -> {

            if (items != null) {
              SalesData saleData = new SalesData();
              BigDecimal sumValue = sale.getSaleValue();

              saleData.setSaleValue(
                  items.getItemPrice().multiply(BigDecimal.valueOf(items.getItemQuantity())));
              sumValue = saleData.getSaleValue().add(sumValue);
              sale.setSaleValue(sumValue);
            }

          })

      );

    });

    return true;

  }

  public LayoutFile populateLayout(String idSaleExpensive, long countSellers, long countClient,
      String nameWorstSeller) {

    LayoutFile layoutFile = new LayoutFile();

    layoutFile.setIdSaleExpensive(idSaleExpensive);
    layoutFile.setCountSellers(countSellers);
    layoutFile.setCountClient(countClient);
    layoutFile.setNameWorstSeller(nameWorstSeller);

    return layoutFile;

  }

}
