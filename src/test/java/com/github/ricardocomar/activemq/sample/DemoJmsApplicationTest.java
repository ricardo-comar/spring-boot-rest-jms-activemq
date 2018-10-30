package com.github.ricardocomar.activemq.sample;


import java.text.DateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.ricardocomar.activemq.sample.DemoJmsApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=DemoJmsApplication.class)
public class DemoJmsApplicationTest {

	@Autowired private JmsTemplate jmsTemplate;
    @Autowired private JmsTemplate jmsTemplateTopic;

	@Test
	public void publishOneMessage() throws Exception {
  	String msg = "{user: 'homer.simpson', delay: '10s', template: 'topico', now: '" + DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()) + "'}";
  	System.err.println("Msg: " + msg);
  	jmsTemplateTopic.convertAndSend("topic.sample", msg,
      		m -> {m.clearProperties(); m.setLongProperty("AMQ_SCHEDULED_DELAY", 10*1000); return m; });

	}
}