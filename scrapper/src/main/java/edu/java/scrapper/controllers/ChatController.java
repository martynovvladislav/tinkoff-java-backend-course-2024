package edu.java.scrapper.controllers;

import edu.java.scrapper.exceptions.TooManyRequestsException;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.utils.BucketGrabber;
import io.github.bucket4j.Bucket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final BucketGrabber bucketGrabber;

    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable("id") Long tgChatId
    ) {
        Bucket bucket = bucketGrabber.grabBucket(tgChatId);
        if (bucket.tryConsume(1)) {
            chatService.register(tgChatId);
            log.info("The chat has been registered");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException();
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable("id") Long tgChatId
    ) {
        Bucket bucket = bucketGrabber.grabBucket(tgChatId);
        if (bucket.tryConsume(1)) {
            chatService.unregister(tgChatId);
            log.info("The chat has been deleted");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        throw new TooManyRequestsException();
    }
}
