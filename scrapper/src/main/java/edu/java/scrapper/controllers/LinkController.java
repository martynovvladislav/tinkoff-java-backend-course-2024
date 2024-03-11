package edu.java.scrapper.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LinkController {
    @PostMapping("/tg-chat/{id}")
    public ResponseEntity<Void> registerChat(
        @PathVariable("id") Long tgChatId
    ) {
        log.info("The chat has been registered");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/tg-chat/{id}")
    public ResponseEntity<Void> deleteChat(
        @PathVariable("id") Long tgChatId
    ) {
        log.info("The chat has been deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
