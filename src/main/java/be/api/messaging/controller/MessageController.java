package be.api.messaging.controller;

import be.api.messaging.dto.ImageMessage;
import be.api.messaging.dto.TextMessage;
import be.api.messaging.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/text")
    public ResponseEntity<Void> sendTextMessage(@RequestBody TextMessage message) {
        messageService.sendTextMessage(message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> sendImageMessage(
            @RequestParam("sender") String sender,
            @RequestParam("receiver") String receiver,
            @RequestParam("image") MultipartFile image) throws IOException {

        ImageMessage message = new ImageMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setImageData(image.getBytes());
        message.setImageType(image.getContentType());

        messageService.sendImageMessage(message);
        return ResponseEntity.ok().build();
    }
}
