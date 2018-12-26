# spring-boot-rest-jms-activemq

Simple PoC project to demonstrate a "delay" feature on ActiveMQ.

Created using Spring Boot 2.0, with Maven 3 and [Actuator](http://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html#production-ready-endpoints). 

Also has a docker-compose to start a local instance of ActiveMQ :)

## Usage
- Download the repo
- Open 3 consoles (or use _terminator_ to split in 3 spaces - I suggest 3 horizontal)

### 1st console
- run `docker-compose up` to start an ActiveMQ instance

### 2nd console
- Start main application with `mvn spring-boot:run`and keep it running.

### 3rd console
- Run `mvn test` to send sample messages 

## How it works
- The [MessageListenerComponent](src/main/java/com/github/ricardocomar/activemq/sample/MessageListenerComponent.java) register to a sample queue and a sample topic.
- The [DemoJmsApplicationTest](src/test/java/com/github/ricardocomar/activemq/sample/DemoJmsApplicationTest.java) post two messages, both with a header property **AMQ_SCHEDULED_DELAY** with a numeric value, indicating in milliseconds to wait until the message will be available on queue (or topic).
- You can monitor both on ActiveMQ Console in [queues](http://localhost:8161/admin/queues.jsp) and [topics](http://localhost:8161/admin/topics.jsp).
- A few seconds after, you will see the consumer showing the messages delivered, with the original time and delay.
```
2018-10-30 09:15:29.012  INFO : Queue: {user: 'homer.simpson', delay: '5s', template: 'fila', now: '09:15:23'}
2018-10-30 09:15:34.002  INFO : Topic: {user: 'homer.simpson', delay: '10s', template: 'topico', now: '09:15:23'}
```

## MQ References
- http://activemq.apache.org/delay-and-schedule-message-delivery.html
- https://receitasdecodigo.com.br/spring-boot/exemplo-de-projeto-com-spring-boot-jms-activemq-usando-filas-e-topicos

## Job References
- https://stackoverflow.com/questions/46974272/spring-boot-add-new-schedule-job-dynamically
- https://stackoverflow.com/questions/42789209/no-qualifying-bean-of-type-scheduledexecutorservice-taskscheduler
- https://g00glen00b.be/spring-batch-schedule/
- https://docs.spring.io/spring-batch/trunk/reference/html/configureJob.html#inMemoryRepository
- https://stackoverflow.com/questions/39913918/spring-boot-spring-batch-without-datasource
- http://blogs.innovationm.com/job-scheduling-with-spring-batch/
- https://docs.spring.io/spring/docs/4.1.5.RELEASE/spring-framework-reference/html/scheduling.html
