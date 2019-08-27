package com.github.ricardocomar.activemq.sample.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = { "demo.config.service.country.url=http://localhost:8888/rest/v2", })
@ContextConfiguration(classes = CountrySearchService.class)
public class CountrySearchServiceTest {

	private static final ObjectMapper MAPPER = new ObjectMapper().configure(
			DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration
			.options().port(8888).httpsPort(8889)
			.extensions(new ResponseTemplateTransformer(false)));

	@Autowired
	private CountrySearchService service;

	@Test
	public void testSuccess() throws Exception {

		CountryResponse[] expected = MAPPER.readValue(
				new String(Files.readAllBytes(Paths.get(ClassLoader
						.getSystemResource(
								"__files/rest_v2_name_springfield_body.json")
						.toURI()))), CountryResponse[].class);

		String responseBody = service.searchCountry("springfield");
		CountryResponse[] response = MAPPER.readValue(responseBody,
				CountryResponse[].class);
		Assert.assertThat(response[0].getError(),
				Matchers.nullValue());
		Assert.assertThat(response[0].getName(),
				Matchers.is(Matchers.equalTo(expected[0].getName())));
	}

	@Test
	public void testInvalid() throws Exception {

		String responseBody = service.searchCountry("xxx");
		CountryResponse[] response = MAPPER.readValue(responseBody,
				CountryResponse[].class);
		Assert.assertThat(response[0].getName(),
				Matchers.nullValue());
		Assert.assertThat(response[0].getError(),
				Matchers.containsString("invalid country"));
	}

	public static final class CountryResponse {
		private String name;
		private String error;
		
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
