package com.agibank.app.batch_processing_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.agibank.app.batch_processing_app.core.BatchProcessingCore;

@SpringBootApplication
public class App 
{		

	public static void main( String[] args )
    {
		SpringApplication.run(App.class, args);
    	BatchProcessingCore core = new BatchProcessingCore();
    	core.startProcessing();
	
    }
}
