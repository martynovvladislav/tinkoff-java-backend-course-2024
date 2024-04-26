package edu.java.bot.controllers;

import edu.java.bot.dtos.LinkUpdateDto;
import edu.java.bot.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {
    private final NotificationService notificationService;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
        @Valid
        @RequestBody
        LinkUpdateDto linkUpdateDto
    ) {
        notificationService.sendUpdates(linkUpdateDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
