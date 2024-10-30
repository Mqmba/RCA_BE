package be.api.messaging.listener;

import be.api.messaging.dto.ImageMessage;
import be.api.messaging.dto.TextMessage;
import be.api.messaging.service.MessageProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CollectorMessageListener {
    private final MessageProcessor messageProcessor;

    @RabbitListener(queues = "${rabbitmq.queue.collector}")
    public void handleTextMessage(TextMessage message) {
        log.info("Collector received text message from: {} to: {}",
                message.getSender(), message.getReceiver());

        try {
            messageProcessor.processAndForwardTextMessage(message, "COLLECTOR");
            log.info("Successfully processed text message for collector: {}",
                    message.getReceiver());
        } catch (Exception e) {
            log.error("Error processing text message for collector: {}",
                    message.getReceiver(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.collector}")
    public void handleImageMessage(ImageMessage message) {
        log.info("Collector received image message from: {} to: {}",
                message.getSender(), message.getReceiver());

        try {
            messageProcessor.processAndForwardImageMessage(message, "COLLECTOR");
            log.info("Successfully processed image message for collector: {}",
                    message.getReceiver());
        } catch (Exception e) {
            log.error("Error processing image message for collector: {}",
                    message.getReceiver(), e);
        }
    }
}
