package edu.java.bot.services;

import edu.java.bot.dtos.LinkUpdateDto;
import edu.java.bot.suppliers.MessageSupplier;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final MessageSupplier messageSupplier;

    @KafkaListener(topics = "${kafka.kafka-topic-name}", containerFactory = "containerFactory")
    public void processLinkUpdateEvent(@Payload LinkUpdateDto linkUpdateDto, Acknowledgment acknowledgment) {
        log.info("Received new update: " + linkUpdateDto);
        sendUpdates(linkUpdateDto);
        acknowledgment.acknowledge();
    }

    public void sendUpdates(LinkUpdateDto linkUpdateDto) {
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
    }
}
