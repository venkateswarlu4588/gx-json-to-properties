package com.gx.jsontoproperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gx.jsontoproperties.service.JsonToPropsConverterService;

@SpringBootApplication
public class GxJsonToPropertiesApplication implements CommandLineRunner {
	
	@Autowired
	JsonToPropsConverterService jsonConvertService;
	
	public static void main(String[] args) {
		SpringApplication.run(GxJsonToPropertiesApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		jsonConvertService.jsonToPropsParse();
	}

}
