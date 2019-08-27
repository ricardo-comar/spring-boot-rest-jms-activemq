package com.github.ricardocomar.activemq.sample.config;

import java.io.IOException;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig extends DefaultBatchConfigurer {

	@Bean
	public RestTemplate restTemplate(@Autowired ResponseErrorHandler errorHandler) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(errorHandler);

		return restTemplate;
	}

	@Bean
	public ResponseErrorHandler responseErrorHandler() {
		return new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse resp) throws IOException {
				return resp.getStatusCode().is4xxClientError()
						|| resp.getStatusCode().is5xxServerError();
			}

			@Override
			public void handleError(ClientHttpResponse resp) throws IOException {
			}
		};
	}
}
