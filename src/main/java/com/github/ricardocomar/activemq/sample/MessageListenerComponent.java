package com.github.ricardocomar.activemq.sample;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListenerComponent {

    @Autowired private JmsTemplate jmsTemplate;
    @Autowired private JmsTemplate jmsTemplateTopic;
    
    private static final Logger LOGGER = Logger.getLogger("Listener"); 


    @JmsListener(destination = "queue.sample")
    public void onReceiverQueue(String str) {
    	LOGGER.info("Queue: "+ str );
    }

    @JmsListener(destination = "topic.sample", containerFactory = "jmsFactoryTopic")
    public void onReceiverTopic(String str) {
        LOGGER.info("Topic: " + str );
    }

}