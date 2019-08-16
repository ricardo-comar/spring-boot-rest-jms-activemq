package com.github.ricardocomar.activemq.sample.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

@Configuration
@Profile("wiremock")
public class WiremockConfig {

	@Value("${wiremock.port}")
	private String wmPort;

	@PostConstruct
	public void postConstruct() {
		WireMockServer wireMockServer = new WireMockServer(
				WireMockConfiguration.options().port(Integer.valueOf(wmPort)));
		wireMockServer.start();
	}

}
