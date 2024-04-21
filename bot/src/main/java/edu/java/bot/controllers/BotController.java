package edu.java.bot.controllers;

import edu.java.bot.dtos.LinkUpdateDto;
import edu.java.bot.suppliers.MessageSupplier;
import jakarta.validation.Valid;
import java.util.List;
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
    private final MessageSupplier messageSupplier;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
        @Valid
        @RequestBody
        LinkUpdateDto linkUpdateDto
    ) {
        String description = linkUpdateDto.getDescription();
        String url = linkUpdateDto.getUrl().toString();
        List<Long> tgChatIds = linkUpdateDto.getTgChatIds();
        for (Long tgChatId : tgChatIds) {
            messageSupplier.send(
                tgChatId,
                description + "\n" + url
            );
        }
        log.info("Update has been sent");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
