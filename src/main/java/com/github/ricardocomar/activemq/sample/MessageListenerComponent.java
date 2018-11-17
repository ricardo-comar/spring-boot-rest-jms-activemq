package com.github.ricardocomar.activemq.sample;

import java.util.logging.Logger;

import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerComponent {

	private static final Logger LOGGER = Logger.getLogger("Listener"); 

//    public static final String TOPIC_SAMPLE = "topic.sample";
//    public static final String QUEUE_SAMPLE = "topic.sample";

    @JmsListener(destination = JmsConfig.ORDER_QUEUE)
    public void onReceiverQueue(@Payload DemoMessage message,
            @Headers MessageHeaders headers,
            Message msg, Session session) {
    	LOGGER.info("Queue: "+ message );
    }

//    @JmsListener(destination = TOPIC_SAMPLE, containerFactory = "jmsFactoryTopic")
//    public void onReceiverTopic(@Payload DemoMessage message,
//            @Headers MessageHeaders headers,
//            Message msg, Session session) {
//        LOGGER.info("Topic: " + message );
//    }

}