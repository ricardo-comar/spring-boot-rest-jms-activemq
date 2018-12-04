package com.github.ricardocomar.activemq.sample;

import java.io.IOException;
import java.lang.reflect.Parameter;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpoint;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.messaging.handler.annotation.Payload;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomJmsListenerContainerFactory extends
		DefaultJmsListenerContainerFactory {

	private Jackson2ObjectMapperBuilder builder;

	public CustomJmsListenerContainerFactory(Jackson2ObjectMapperBuilder builder) {
		this.builder = builder;
	}

	@Override
	public DefaultMessageListenerContainer createListenerContainer(
			JmsListenerEndpoint endpoint) {
		
		if (endpoint instanceof MethodJmsListenerEndpoint) {
			MethodJmsListenerEndpoint methodEndpoint = (MethodJmsListenerEndpoint) endpoint;
			

			Class<?> mappedPayload = null;
			for (Parameter param : methodEndpoint.getMethod().getParameters()) {
				if (param.isAnnotationPresent(Payload.class)) {
					mappedPayload = param.getType();
					break;
				}
			}
			
			if (mappedPayload == null) {
				throw new IllegalStateException("Parameter with annotation \"Payload\" not found");
			}
			
			final MappingJackson2MessageConverter converter = initConverter(mappedPayload);
			super.setMessageConverter(converter);
		}
		
		return super.createListenerContainer(endpoint);
	}

	private MappingJackson2MessageConverter initConverter(Class<?> mappedClass) {
		
		ObjectMapper objectMapper = builder.createXmlMapper(false)
				.modules(new Jdk8Module(), new JavaTimeModule()).build();

		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter() {

			@Override
			public Object fromMessage(Message message) throws JMSException,
					MessageConversionException {

				JavaType targetJavaType = objectMapper.getTypeFactory()
						.constructType(mappedClass);

				try {
					return convertMessage(message, targetJavaType);
				} catch (IOException e) {
					throw new MessageConversionException(
							"Failed to convert JSON message content", e);
				}
			}

			private Object convertMessage(Message message,
					JavaType targetJavaType) throws JMSException, IOException {
				
				if (message instanceof TextMessage) {
					return convertFromTextMessage((TextMessage) message,
							targetJavaType);
				} else if (message instanceof BytesMessage) {
					return convertFromBytesMessage((BytesMessage) message,
							targetJavaType);
				} else {
					return convertFromMessage(message, targetJavaType);
				}
			}

		};
		converter.setObjectMapper(objectMapper);
		converter.setTargetType(MessageType.TEXT);
		return converter;
	}

}
