package com.github.ricardocomar.activemq.sample;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostEndpoint {

	private static final Logger LOGGER = Logger.getLogger("Endpoint"); 
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsTemplate jmsTemplateTopic;

	@PostMapping("/message/topic")
	public ResponseEntity<String> topic(@RequestBody DemoMessage message) {

		LOGGER.info("Msg: " + message);
		jmsTemplateTopic.convertAndSend(MessageListenerComponent.TOPIC_SAMPLE, message, m -> {
			m.clearProperties();
			m.setLongProperty("AMQ_SCHEDULED_DELAY", 10 * 1000);
			return m;
		});

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/message/queue")
	public ResponseEntity<String> queue(@RequestBody DemoMessage message) {

		LOGGER.info("Msg: " + message);
		jmsTemplate.convertAndSend(MessageListenerComponent.QUEUE_SAMPLE, message, m -> {
			m.clearProperties();
			m.setLongProperty("AMQ_SCHEDULED_DELAY", 5 * 1000);
			return m;
		});

		return ResponseEntity.noContent().build();
	}

}
