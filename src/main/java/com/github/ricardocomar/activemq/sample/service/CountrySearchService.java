package com.github.ricardocomar.activemq.sample.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.mockito.internal.util.io.IOUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Service
public class CountrySearchService {
	
	@Value("${demo.config.service.country.url}")
	private String endpointUrl;

	private static final Logger LOGGER = Logger.getLogger("Search Service");

	public String searchCountry(String query) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			
			@Override
			public boolean hasError(ClientHttpResponse resp) throws IOException {
				return resp.getStatusCode().is4xxClientError() || resp.getStatusCode().is5xxServerError() ;
			}
			
			@Override
			public void handleError(ClientHttpResponse resp) throws IOException {
			}
		});
		
		LOGGER.info("Query: " + query);
		String url = endpointUrl + "/name/" + query;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		LOGGER.info("Response code: " + response.getStatusCode());
		String content = response.getBody();
		LOGGER.info("Response content: " + content);
		
		return content;
	}
}
