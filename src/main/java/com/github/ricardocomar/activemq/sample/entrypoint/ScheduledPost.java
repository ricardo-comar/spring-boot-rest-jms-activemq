package com.github.ricardocomar.activemq.sample.entrypoint;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.activemq.sample.config.JmsConfig;
import com.github.ricardocomar.activemq.sample.model.DemoMessage;

@Component
@EnableScheduling
public class ScheduledPost {
	
	private static final Logger LOGGER = Logger.getLogger("ScheduledPost");

	@Autowired
	private JmsTemplate jmsTemplateTopic;

	@Autowired
	private ObjectMapper objectMapper;

	@Scheduled(cron = "*/3 * * * * *")    
	public void postMessage() throws Exception {
		
		DemoMessage message = new DemoMessage();
		message.setMessage("brasil");
		
		LOGGER.info("Batch: " + objectMapper.writeValueAsString(message));
		
		jmsTemplateTopic.convertAndSend(JmsConfig.TOPIC_SAMPLE, message);
	}
}
