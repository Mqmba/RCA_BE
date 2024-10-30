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
public class ResidentMessageListener {
    private final MessageProcessor messageProcessor;

    @RabbitListener(queues = "${rabbitmq.queue.resident}")
    public void handleTextMessage(TextMessage message) {
        log.info("Resident received text message from: {} to: {}",
                message.getSender(), message.getReceiver());

        try {
            messageProcessor.processAndForwardTextMessage(message, "RESIDENT");
            log.info("Successfully processed text message for resident: {}",
                    message.getReceiver());
        } catch (Exception e) {
            log.error("Error processing text message for resident: {}",
                    message.getReceiver(), e);
        }
    }

    @RabbitListener(queues = "${rabbitmq.queue.resident}")
    public void handleImageMessage(ImageMessage message) {
        log.info("Resident received image message from: {} to: {}",
                message.getSender(), message.getReceiver());

        try {
            messageProcessor.processAndForwardImageMessage(message, "RESIDENT");
            log.info("Successfully processed image message for resident: {}",
                    message.getReceiver());
        } catch (Exception e) {
            log.error("Error processing image message for resident: {}",
                    message.getReceiver(), e);
        }
    }
}
