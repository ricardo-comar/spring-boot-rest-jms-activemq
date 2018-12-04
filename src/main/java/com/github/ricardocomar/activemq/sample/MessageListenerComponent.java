package com.github.ricardocomar.activemq.sample;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageListenerComponent {

	private static final Logger LOGGER = Logger.getLogger("Listener");

	@Autowired
	private ObjectMapper objectMapper;

	@JmsListener(destination = JmsConfig.QUEUE_SAMPLE, containerFactory = "queueContainerFactory")
	public void onReceiverQueue(@Headers MessageHeaders headers,
			@Payload DemoMessage message, Session session) throws Exception {

		message.setAck(Boolean.TRUE);
		message.setRead(LocalDateTime.now());
		LOGGER.info("Queue: " + objectMapper.writeValueAsString(message));
	}

	@JmsListener(destination = JmsConfig.TOPIC_SAMPLE, containerFactory = "topicJmsListenerContainerFactory")
	public void onReceiverTopic(@Headers MessageHeaders headers,
			@Payload DemoMessage message, Session session) throws Exception {

		message.setAck(Boolean.TRUE);
		message.setRead(LocalDateTime.now());
		LOGGER.info("Topic: " + objectMapper.writeValueAsString(message));
	}

}