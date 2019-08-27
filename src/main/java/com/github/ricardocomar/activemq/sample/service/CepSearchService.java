package com.github.ricardocomar.activemq.sample.service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepSearchService {
	
	@Value("${demo.config.service.cep.url}")
	private String endpointUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	private static final Logger LOGGER = Logger.getLogger("Search Service");

	public ResponseEntity<String> searchCep(String query) {
		LOGGER.info("Query: " + query);
		Map<String,Object> uriVars = new HashMap<String, Object>();
		uriVars.put("cep", query);
		
		ResponseEntity<String> response = restTemplate.getForEntity(endpointUrl, String.class, uriVars );

		LOGGER.info("Response code: " + response.getStatusCode());
		LOGGER.info("Response content: " + response.getBody());
		
		return response;
	}
}
