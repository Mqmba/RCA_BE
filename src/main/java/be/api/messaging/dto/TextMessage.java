package be.api.messaging.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TextMessage implements Serializable {
    private String sender;
    private String receiver;
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();
}
