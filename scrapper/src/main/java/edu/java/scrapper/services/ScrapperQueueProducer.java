package edu.java.scrapper.services;

import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer {
    private final KafkaTemplate<String, LinkUpdateDto> kafkaProducer;

    public void send(LinkUpdateDto linkUpdateDto, String topicName) {
        log.info(linkUpdateDto.toString());
        log.info(topicName);
        Message<LinkUpdateDto> message = MessageBuilder
            .withPayload(linkUpdateDto)
            .setHeader(KafkaHeaders.TOPIC, topicName)
            .build();
        kafkaProducer.send(message);
    }
}
