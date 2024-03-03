package edu.java.bot.controllers;

import edu.java.bot.dtos.LinkUpdate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BotController {
    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
        @Valid
        @RequestBody
        LinkUpdate linkUpdate
    ) {
        log.info("Update has been sent");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
