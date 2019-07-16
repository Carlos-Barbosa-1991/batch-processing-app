package com.agibank.app.batch.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.agibank.app.batch.processing.core.BatchProcessingCore;
import com.agibank.app.batch.processing.service.BatchProcessingService;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		BatchProcessingService service = new BatchProcessingService();
		service.startProcessing();

	}
}
