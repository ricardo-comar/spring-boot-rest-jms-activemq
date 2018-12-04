package com.github.ricardocomar.activemq.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class MessageConverterConfiguration {
	
	private static final String TYPE_ID_PROP = "_type";

	@Bean
	public MessageConverter messageConverter(final ObjectMapper objectMapper) {

		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setObjectMapper(objectMapper);
		converter.setTypeIdPropertyName(TYPE_ID_PROP);
		converter.setTargetType(MessageType.TEXT);
		return converter;
	}

	@Bean
	public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder builder) {

		return builder.createXmlMapper(false)
				.modules(new Jdk8Module(), new JavaTimeModule()).build();
	}
}
