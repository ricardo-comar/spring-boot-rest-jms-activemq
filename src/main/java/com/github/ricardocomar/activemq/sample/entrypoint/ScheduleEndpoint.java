package com.github.ricardocomar.activemq.sample.entrypoint;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardocomar.activemq.sample.config.JmsConfig;
import com.github.ricardocomar.activemq.sample.model.DemoMessage;

@RestController
public class ScheduleEndpoint {

	private static final Logger LOGGER = Logger.getLogger("Endpoint");

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TaskScheduler executor;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsTemplate jmsTemplateTopic;

	@PostMapping("/job/topic")
	public ResponseEntity<String> topic(@RequestBody DemoMessage message)
			throws Exception {

		message.setReceived(LocalDateTime.now());
		LOGGER.info("Msg: " + objectMapper.writeValueAsString(message));

		Runnable task = new Runnable() {

			@Override
			public void run() {
				jmsTemplateTopic
						.convertAndSend(JmsConfig.TOPIC_SAMPLE, message);
			}
		};
		executor.schedule(
				task,
				Date.from(LocalDateTime.now().plusSeconds(10)
						.atZone(ZoneId.systemDefault()).toInstant()));

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/job/queue")
	public ResponseEntity<String> queue(@RequestBody DemoMessage message)
			throws Exception {

		message.setReceived(LocalDateTime.now());
		LOGGER.info("Msg: " + objectMapper.writeValueAsString(message));

		Runnable task = new Runnable() {

			@Override
			public void run() {
				jmsTemplate.convertAndSend(JmsConfig.QUEUE_SAMPLE, message);
			}
		};
		
		executor.schedule(
				task,
				Date.from(LocalDateTime.now().plusSeconds(10)
						.atZone(ZoneId.systemDefault()).toInstant()));

		return ResponseEntity.noContent().build();
	}

}
