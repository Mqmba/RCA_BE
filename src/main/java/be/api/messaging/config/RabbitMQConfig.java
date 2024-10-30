package be.api.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.resident}")
    private String residentQueue;

    @Value("${rabbitmq.queue.collector}")
    private String collectorQueue;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.resident}")
    private String residentRoutingKey;

    @Value("${rabbitmq.routing.collector}")
    private String collectorRoutingKey;

    @Bean
    public Queue residentQueue() {
        return new Queue(residentQueue, true);
    }

    @Bean
    public Queue collectorQueue() {
        return new Queue(collectorQueue, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding residentBinding() {
        return BindingBuilder
                .bind(residentQueue())
                .to(exchange())
                .with(residentRoutingKey);
    }

    @Bean
    public Binding collectorBinding() {
        return BindingBuilder
                .bind(collectorQueue())
                .to(exchange())
                .with(collectorRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
