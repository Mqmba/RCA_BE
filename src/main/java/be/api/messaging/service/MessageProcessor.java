package be.api.messaging.service;

import be.api.messaging.dto.ImageMessage;
import be.api.messaging.dto.TextMessage;
import be.api.messaging.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class MessageProcessor {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    public void processAndForwardTextMessage(TextMessage message, String userType) {
        ChatMessage chatMessage = ChatMessage.builder()
                .messageId(generateMessageId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .content(message.getContent())
                .timestamp(message.getTimestamp().format(TIME_FORMATTER))
                .messageType("TEXT")
                .userType(userType)
                .status("DELIVERED")
                .build();

        String destination = "/topic/chat/" + message.getReceiver();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    public void processAndForwardImageMessage(ImageMessage message, String userType) {
        String base64Image = Base64.getEncoder().encodeToString(message.getImageData());

        ChatMessage chatMessage = ChatMessage.builder()
                .messageId(generateMessageId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .content(base64Image)
                .timestamp(message.getTimestamp().format(TIME_FORMATTER))
                .messageType("IMAGE")
                .imageType(message.getImageType())
                .userType(userType)
                .status("DELIVERED")
                .build();

        String destination = "/topic/chat/" + message.getReceiver();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

    private String generateMessageId() {
        return java.util.UUID.randomUUID().toString();
    }
}
