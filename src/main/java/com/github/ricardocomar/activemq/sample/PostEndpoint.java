package com.github.ricardocomar.activemq.sample;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PostEndpoint {

	private static final Logger LOGGER = Logger.getLogger("Endpoint"); 
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsTemplate jmsTemplateTopic;

	@PostMapping("/message/topic")
	public ResponseEntity<String> topic(@RequestBody String message) {

		LOGGER.info("Msg: " + message);
//		jmsTemplateTopic.convertAndSend(JmsConfig.TOPIC_SAMPLE, message);
		jmsTemplateTopic.convertAndSend(JmsConfig.TOPIC_SAMPLE, message, m -> {
			m.clearProperties();
			m.setLongProperty("AMQ_SCHEDULED_DELAY", 10 * 1000);
			return m;
		});

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/message/queue")
	public ResponseEntity<String> queue(@RequestBody String message) {

		LOGGER.info("Msg: " + message);
		jmsTemplate.convertAndSend(JmsConfig.QUEUE_SAMPLE, message, m -> {
			m.clearProperties();
			m.setLongProperty("AMQ_SCHEDULED_DELAY", 5 * 1000);
			return m;
		});
//		jmsTemplate.convertAndSend(JmsConfig.QUEUE_SAMPLE, message);

		return ResponseEntity.noContent().build();
	}

}
