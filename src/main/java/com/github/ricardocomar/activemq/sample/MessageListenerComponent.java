package com.github.ricardocomar.activemq.sample;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageListenerComponent {

	private static final Logger LOGGER = Logger.getLogger("Listener"); 

	@Autowired
	private ObjectMapper objectMapper;

    @JmsListener(destination = JmsConfig.QUEUE_SAMPLE, containerFactory="queueContainerFactory")
    public void onReceiverQueue(@Payload String message,
            @Headers MessageHeaders headers,
            Message msg, Session session) throws Exception {
    	
    	DemoMessage demoMsg = objectMapper.readValue(message, DemoMessage.class);
    	demoMsg.setAck(Boolean.TRUE.toString());
    	
    	LOGGER.info("Queue: "+ message );
    }

    @JmsListener(destination = JmsConfig.TOPIC_SAMPLE, containerFactory = "topicJmsListenerContainerFactory")
    public void onReceiverTopic(@Payload String message,
            @Headers MessageHeaders headers,
            Message msg, Session session) throws Exception {

    	DemoMessage demoMsg = objectMapper.readValue(message, DemoMessage.class);
    	demoMsg.setAck(Boolean.TRUE.toString());
        LOGGER.info("Topic: " + message );
    }

}