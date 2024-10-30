package be.api.messaging.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
    private String messageId;
    private String sender;
    private String receiver;
    private String content;
    private String timestamp;
    private String messageType;
    private String imageType;
    private String userType;
    private String status;
}
