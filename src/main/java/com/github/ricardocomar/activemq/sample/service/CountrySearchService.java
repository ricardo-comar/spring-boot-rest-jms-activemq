package com.github.ricardocomar.activemq.sample.service;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CountrySearchService {
	
	@Value("${demo.config.service.country.url}")
	private String endpointUrl;

	private static final Logger LOGGER = Logger.getLogger("Search Service");

	public void searchCountry(String query) {
		RestTemplate restTemplate = new RestTemplate();
		
		LOGGER.info("Query: " + query);
		String url = endpointUrl + "/name/" + query;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		LOGGER.info("Response code: " + response.getStatusCode());
		LOGGER.info("Response content: " + response.getBody());
	}
}
