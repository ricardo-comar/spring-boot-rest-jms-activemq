package com.github.ricardocomar.activemq.sample.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.activemq.sample.config.RestConfig;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "demo.config.service.cep.url=http://localhost:8888/ws/{cep}/json/", })
@ContextConfiguration(classes = { RestConfig.class, CepSearchService.class })
public class CepSearchServiceTest {

	private static final ObjectMapper MAPPER = new ObjectMapper().configure(
			DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration
			.options().port(8888).httpsPort(8889)
			.extensions(new ResponseTemplateTransformer(false)));

	@Autowired
	private CepSearchService service;
	
	@Test
	public void testSuccess() throws Exception {
		
		CepResponse expected = MAPPER.readValue(
				new String(Files.readAllBytes(Paths.get(ClassLoader
						.getSystemResource(
								"__files/rest_v2_cep_01001000_body.json")
						.toURI()))), CepResponse.class);

		ResponseEntity<String> responseBody = service.searchCep("01001000");
		Assert.assertThat(responseBody.getStatusCode(),
				Matchers.equalTo(HttpStatus.OK));
		CepResponse response = MAPPER
				.readValue(responseBody.getBody(), CepResponse.class);
		
		Assert.assertThat(response.getError(), Matchers.nullValue());
		Assert.assertThat(response.getLogradouro(),
				Matchers.is(Matchers.equalTo(expected.getLogradouro())));
	}

	@Test
	public void testError() throws Exception {

		ResponseEntity<String> responseBody = service.searchCep("91234567");
		Assert.assertThat(responseBody.getStatusCode(),
				Matchers.equalTo(HttpStatus.NOT_FOUND));
		CepResponse response = MAPPER
				.readValue(responseBody.getBody(), CepResponse.class);
		
		Assert.assertThat(response.getLogradouro(), Matchers.nullValue());
		Assert.assertThat(response.getError(),
				Matchers.containsString("Wiremock error cenario for CEP"));
	}

	@Test
	public void testInconsistent() throws Exception {

		ResponseEntity<String> responseBody = service.searchCep("xxx");
		Assert.assertThat(responseBody.getStatusCode(),
				Matchers.equalTo(HttpStatus.I_AM_A_TEAPOT));
		CepResponse response = MAPPER
				.readValue(responseBody.getBody(), CepResponse.class);
		
		Assert.assertThat(response.getLogradouro(), Matchers.nullValue());
		Assert.assertThat(response.getError(),
				Matchers.equalTo("Inconsistent call"));
	}

	public static final class CepResponse {
		private String error;
		private String logradouro;

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}

		public String getLogradouro() {
			return logradouro;
		}

		public void setLogradouro(String logradouro) {
			this.logradouro = logradouro;
		}
	}
}
