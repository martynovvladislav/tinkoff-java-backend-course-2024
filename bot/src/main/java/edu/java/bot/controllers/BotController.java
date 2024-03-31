package edu.java.bot.controllers;

import edu.java.bot.dtos.LinkUpdateDto;
import edu.java.bot.exceptions.TooManyRequestsException;
import edu.java.bot.suppliers.MessageSupplier;
import edu.java.bot.utils.BucketGrabber;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
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
    private final BucketGrabber bucketGrabber;

    @PostMapping("/updates")
    public ResponseEntity<Void> sendMessage(
        @Valid
        @RequestBody
        LinkUpdateDto linkUpdateDto,
        HttpServletRequest request
    ) {
        String clientIP = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
            .orElseGet(request::getRemoteAddr);
        Bucket bucket = bucketGrabber.grabBucket(String.valueOf(clientIP));
        if (bucket.tryConsume(1)) {
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
        } else {
            throw new TooManyRequestsException();
        }
    }
}
