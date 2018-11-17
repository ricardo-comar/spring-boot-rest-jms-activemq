package com.github.ricardocomar.activemq.sample;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@EnableJms
@Configuration
public class JmsConfig {

	public static final String TOPIC_SAMPLE = "topic.sample";
	public static final String QUEUE_SAMPLE = "topic.sample";

	// @Bean
	// public JmsListenerContainerFactory<?> queueListenerFactory() {
	// DefaultJmsListenerContainerFactory factory = new
	// DefaultJmsListenerContainerFactory();
	// factory.setMessageConverter(messageConverter());
	// return factory;
	// }

	@Value("${spring.activemq.broker-url}")
	private String brokerUrl;

	@Value("${spring.activemq.user}")
	private String user;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory(user, password, brokerUrl);
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsFactoryTopic(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		factory.setPubSubDomain(true);
		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(MessageConverter messageConverter,
			ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setMessageConverter(messageConverter);
		return jmsTemplate;
	}

	@Bean
	public JmsTemplate jmsTemplateTopic(MessageConverter messageConverter,
			ConnectionFactory connectionFactory) {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.setMessageConverter(messageConverter);
		return jmsTemplate;
	}

	@Bean
	public MessageConverter messageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

}
