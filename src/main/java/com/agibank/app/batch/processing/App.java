package com.agibank.app.batch.processing;

import com.agibank.app.batch.processing.service.BatchProcessingService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
    BatchProcessingService service = new BatchProcessingService();
    service.startProcessing();

  }
}
