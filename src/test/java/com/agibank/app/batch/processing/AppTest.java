package com.agibank.app.batch.processing;

import com.agibank.app.batch.processing.builder.SalesDataBuilder;
import com.agibank.app.batch.processing.core.BatchProcessingCore;
import com.agibank.app.batch.processing.domain.ClientData;
import com.agibank.app.batch.processing.domain.ConsolidatedData;
import com.agibank.app.batch.processing.domain.SalesData;
import com.agibank.app.batch.processing.domain.SalesItems;
import com.agibank.app.batch.processing.domain.SellerData;
import com.agibank.app.batch.processing.factories.ConsolidatedDataFactory;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

  private final File reportLocation = new File("src\\data\\out\\fileOut.dat");

  public List<ClientData> clientDataTest() {

    List<ClientData> clientData = new ArrayList<>();

    ClientData clientOneBuilder = new ClientData();
    clientOneBuilder.setIdData("1Client");
    clientOneBuilder.setCnpj("0006546897");
    clientOneBuilder.setName("Teste Cliente Um");
    clientOneBuilder.setBusinessArea("rural");
    clientData.add(clientOneBuilder);

    ClientData clientTwoBuilder = new ClientData();
    clientTwoBuilder.setIdData("2Client");
    clientTwoBuilder.setCnpj("00054565464");
    clientTwoBuilder.setName("Teste Cliente Dois");
    clientTwoBuilder.setBusinessArea("urbana");
    clientData.add(clientTwoBuilder);

    return clientData;

  }

  public List<SellerData> sellerDataTest() {

    List<SellerData> sellerData = new ArrayList<>();

    SellerData sellerOneBuilder = new SellerData();
    sellerOneBuilder.setIdData("1Vendedor");
    sellerOneBuilder.setCpf("006548976");
    sellerOneBuilder.setName("Teste Vendedor Um");
    sellerOneBuilder.setSalary(BigDecimal.valueOf(2.00));
    sellerData.add(sellerOneBuilder);

    SellerData sellerTwoBuilder = new SellerData();
    sellerTwoBuilder.setIdData("2Vendedor");
    sellerTwoBuilder.setCpf("000778954646");
    sellerTwoBuilder.setName("Teste Vendedor Dois");
    sellerTwoBuilder.setSalary(BigDecimal.valueOf(3.00));
    sellerData.add(sellerTwoBuilder);

    return sellerData;

  }

  public List<SalesItems> salesItemTest() {

    List<SalesItems> salesItems = new ArrayList<>();

    SalesItems ItemOneBuilder = new SalesItems();
    ItemOneBuilder.setIdItem("1Item");
    ItemOneBuilder.setItemQuantity(20L);
    ItemOneBuilder.setItemPrice(BigDecimal.valueOf(3.00));
    salesItems.add(ItemOneBuilder);

    SalesItems ItemTwoBuilder = new SalesItems();
    ItemTwoBuilder.setIdItem("2Item");
    ItemTwoBuilder.setItemQuantity(40L);
    ItemTwoBuilder.setItemPrice(BigDecimal.valueOf(5.00));
    salesItems.add(ItemTwoBuilder);

    return salesItems;

  }

  public List<SalesData> salesDataTest() {

    List<SalesItems> salesItemsSellerOne = new ArrayList<>(salesItemTest());

    List<SalesItems> salesItemsSellerTwo = new ArrayList<>(salesItemTest());
    salesItemsSellerTwo.get(1).setItemQuantity(50L);
    salesItemsSellerTwo.get(1).setItemPrice(BigDecimal.valueOf(100.00));

    List<SalesData> salesData = new ArrayList<>();

    SalesData saleOneBuilder = new SalesData();
    saleOneBuilder.setIdData("1Vendas");
    saleOneBuilder.setIdSale("1Venda");
    saleOneBuilder.setSalesItems(salesItemsSellerOne);
    saleOneBuilder.setSaleValue(BigDecimal.ZERO);
    saleOneBuilder.setNameSalesman("Teste Vendedor Um");
    salesData.add(saleOneBuilder);

    SalesData saleTwoBuilder = new SalesData();
    saleTwoBuilder.setIdData("2Vendas");
    saleTwoBuilder.setIdSale("2Venda");
    saleTwoBuilder.setSalesItems(salesItemsSellerTwo);
    saleTwoBuilder.setSaleValue(BigDecimal.ZERO);
    saleTwoBuilder.setNameSalesman("Teste Vendedor Dois");
    salesData.add(saleTwoBuilder);

    return salesData;

  }

  @Test
  public void testGenerateReportStatusGenerateReportTrue() {

    ConsolidatedData consolidatedData = new ConsolidatedData();
    BatchProcessingCore core = new BatchProcessingCore();

    consolidatedData.getClientData().addAll(clientDataTest());
    consolidatedData.getSellerData().addAll(sellerDataTest());
    consolidatedData.getSalesData().addAll(salesDataTest());

    Boolean statusGenerateReport = core
        .generateReport(consolidatedData, "teste.dat", reportLocation);
    Assert.assertTrue(statusGenerateReport);

  }

  @Test
  public void testGenerateReportStatusGenerateReportFalse() {

    ConsolidatedData consolidatedData = new ConsolidatedData();
    BatchProcessingCore core = new BatchProcessingCore();

    consolidatedData.getClientData().addAll(clientDataTest());
    consolidatedData.getSellerData().addAll(sellerDataTest());
    consolidatedData.getSalesData().addAll(new ArrayList<>());

    Boolean statusGenerateReport = core
        .generateReport(consolidatedData, "teste.dat", reportLocation);
    Assert.assertFalse(statusGenerateReport);

  }

  @Test
  public void testTransformOk() {

    ArrayList<String> lines = new ArrayList<>();
    lines.add("001ç1234567891234çPedroç50000");
    lines.add("002ç2345675434544345çJose da SilvaçRural");
    lines.add("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro");

    ConsolidatedData consolidatedData = ConsolidatedDataFactory.getConsolidatedData(lines);

    Assert.assertFalse(consolidatedData.getSellerData().isEmpty());
    Assert.assertFalse(consolidatedData.getClientData().isEmpty());
    Assert.assertFalse(consolidatedData.getSalesData().isEmpty());

  }

  @Test
  public void testTransformItemsOk() {

    SalesDataBuilder salesDataBuilder = new SalesDataBuilder();

    String items = "[1-10-100,2-30-2.50,3-40-3.10]";

    List<SalesItems> listItems = salesDataBuilder.transformListItems(items);

    Assert.assertTrue(listItems.size() != 0);

  }

  @Test
  public void testFileOutOfLayout() {

    ArrayList<String> lines = new ArrayList<>();
    lines.add("0011234567891234Pedro50000");
    lines.add("0022345675434544345Jose da SilvaRural");
    lines.add("00310ç[1-10-100,2-30-2.50,3-40-3.10]çPedro");

    ConsolidatedData consolidatedData = ConsolidatedDataFactory.getConsolidatedData(lines);

    Assert.assertTrue(consolidatedData.getSellerData().isEmpty());
    Assert.assertTrue(consolidatedData.getClientData().isEmpty());
    Assert.assertTrue(consolidatedData.getSalesData().isEmpty());

  }

  @Test
  public void testTransformItemsOutOfLayout() {

    SalesDataBuilder salesDataBuilder = new SalesDataBuilder();

    String items = "[110-100,230-2.50,340-3.10]";

    List<SalesItems> listItems = salesDataBuilder.transformListItems(items);

    Assert.assertNull(listItems);

  }

}
