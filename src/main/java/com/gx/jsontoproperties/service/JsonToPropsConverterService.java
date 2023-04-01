package com.gx.jsontoproperties.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JsonToPropsConverterService {

	 @Value("${config.json.input.directory}")
	private String inputLocation;
	 
	 @Value("${config.prop.output.directory}")
	private String outputLocation;
	
	
	public String getInputLocation() {
		return inputLocation;
	}

	public String getOutputLocation() {
		return outputLocation;
	}

	public void jsonToPropsParse() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		File directoryPath = new File(getInputLocation());
		File filesList[] = directoryPath.listFiles();
		for (File file : filesList) {
			JsonNode jsonNode1 = objectMapper.readTree(file);
			LinkedHashMap<String, String> properties = new LinkedHashMap<>();
			convertJsonToProp("", jsonNode1, properties);
			try (OutputStream outputStream = new FileOutputStream(getOutputLocation() + removeExtension(file.getName()) + ".properties")) {
				for (Map.Entry<String, String> entry : properties.entrySet()) {
					outputStream.write((entry.getKey() + "=" + entry.getValue() + "\n").getBytes());
				}
			}
		}
	}
		
	private String removeExtension(String s) {
		return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
	}

	private void convertJsonToProp(String prefix, JsonNode node, LinkedHashMap<String, String> properties) {
		if (node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> it = node.fields();
			while (it.hasNext()) {
				Map.Entry<String, JsonNode> entry = it.next();
				String newPrefix = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
				convertJsonToProp(newPrefix, entry.getValue(), properties);
			}
		} else if (node.isArray()) {
			for (int i = 0; i < node.size(); i++) {
				String newPrefix = prefix + "[" + i + "]";
				convertJsonToProp(newPrefix, node.get(i), properties);
			}
		} else {
			properties.put(prefix, node.asText());
		}
	}

}
