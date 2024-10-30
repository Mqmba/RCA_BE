package be.api.messaging.service;

import be.api.messaging.config.RabbitMQConfig;
import be.api.messaging.dto.ImageMessage;
import be.api.messaging.dto.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.resident}")
    private String residentRoutingKey;

    @Value("${rabbitmq.routing.collector}")
    private String collectorRoutingKey;

    public void sendTextMessage(TextMessage message) {
        String routingKey = determineRoutingKey(message.getReceiver());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    public void sendImageMessage(ImageMessage message) {
        String routingKey = determineRoutingKey(message.getReceiver());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    private String determineRoutingKey(String receiver) {
        return receiver.toLowerCase().contains("resident")
                ? residentRoutingKey
                : collectorRoutingKey;
    }
}
