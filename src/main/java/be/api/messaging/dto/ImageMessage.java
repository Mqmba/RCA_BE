package be.api.messaging.dto;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ImageMessage implements Serializable {
    private String sender;
    private String receiver;
    private byte[] imageData;
    private String imageType;
    private LocalDateTime timestamp = LocalDateTime.now();
}
