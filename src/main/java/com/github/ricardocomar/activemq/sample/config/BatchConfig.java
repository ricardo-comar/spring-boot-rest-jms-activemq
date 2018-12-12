package com.github.ricardocomar.activemq.sample.config;

import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.activemq.sample.model.DemoMessage;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {
	
	private static final Logger LOGGER = Logger.getLogger("Batch");

	@Autowired
	private JmsTemplate jmsTemplateTopic;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public void setDataSource(DataSource dataSource) {
	}
	
	@Scheduled(cron = "0 * * * * *")    
	public void postMessage() throws Exception {
		
		DemoMessage message = new DemoMessage();
		message.setMessage("Scheduled message");
		
		LOGGER.info("Batch: " + objectMapper.writeValueAsString(message));
		
		jmsTemplateTopic.convertAndSend(JmsConfig.TOPIC_SAMPLE, message);
	}
}
