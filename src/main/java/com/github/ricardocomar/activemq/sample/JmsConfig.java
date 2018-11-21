package com.github.ricardocomar.activemq.sample;

import javax.jms.ConnectionFactory;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

@EnableJms
@Configuration
public class JmsConfig {

	public static final String TOPIC_SAMPLE = "topic.sample";
	public static final String QUEUE_SAMPLE = "queue.sample";

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

	@Bean("cachingConnectionFactory")
	public ConnectionFactory getCachingConnectionFactory(
			ConnectionFactory connectionFactory) {

		return new CachingConnectionFactory(connectionFactory);
	}

	@Bean(name = "queueContainerFactory")
	public JmsListenerContainerFactory<?> queueContainerFactory(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			MessageConverter messageConverter) {

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);

		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setPubSubDomain(false);
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

		return factory;
	}

	@Bean("topicJmsListenerContainerFactory")
	public JmsListenerContainerFactory<?> topicJmsListenerContainerFactory(
			ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer,
			MessageConverter messageConverter) {

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);

		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setPubSubDomain(true);
//		factory.setSubscriptionShared(true);
//		factory.setSubscriptionDurable(true);
		factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

		return factory;
	}

	@Bean
	public JmsTemplate jmsTemplate(
			MessageConverter messageConverter,
			@Qualifier("cachingConnectionFactory") final ConnectionFactory connectionFactory) {
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

}
