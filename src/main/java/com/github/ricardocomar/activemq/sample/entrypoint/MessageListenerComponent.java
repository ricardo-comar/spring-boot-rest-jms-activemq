package com.github.ricardocomar.activemq.sample.entrypoint;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.activemq.sample.config.JmsConfig;
import com.github.ricardocomar.activemq.sample.model.DemoMessage;
import com.github.ricardocomar.activemq.sample.service.CountrySearchService;

@Component
public class MessageListenerComponent {

	private static final Logger LOGGER = Logger.getLogger("Listener");

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CountrySearchService countrySearch;

	@JmsListener(destination = JmsConfig.QUEUE_SAMPLE, containerFactory = "queueContainerFactory", concurrency="1")
	public void onReceiverQueue(@Headers MessageHeaders headers,
			@Payload DemoMessage message, Session session) throws Exception {

		message.setAck(Boolean.TRUE);
		message.setRead(LocalDateTime.now());
		LOGGER.info("Queue: " + objectMapper.writeValueAsString(message));
		
		countrySearch.searchCountry(message.getMessage());
	}

	@JmsListener(destination = JmsConfig.TOPIC_SAMPLE, containerFactory = "topicJmsListenerContainerFactory", concurrency="1")
	public void onReceiverTopic(@Headers MessageHeaders headers,
			@Payload DemoMessage message, Session session) throws Exception {

		message.setAck(Boolean.TRUE);
		message.setRead(LocalDateTime.now());
		LOGGER.info("Topic: " + objectMapper.writeValueAsString(message));

		countrySearch.searchCountry(message.getMessage());
}

}