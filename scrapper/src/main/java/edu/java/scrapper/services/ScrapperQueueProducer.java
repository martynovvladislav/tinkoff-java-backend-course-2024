package edu.java.scrapper.services;

import edu.java.scrapper.clients.bot.BotClient;
import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@RequiredArgsConstructor
@Slf4j
public class ScrapperQueueProducer implements BotClient {
    private final KafkaTemplate<String, LinkUpdateDto> kafkaProducer;

    @Value("${kafka.kafka-topic-name}")
    private String kafkaTopicName;

    public void sendMessage(LinkUpdateDto linkUpdateDto) {
        log.info(linkUpdateDto.toString());
        log.info(kafkaTopicName);
        Message<LinkUpdateDto> message = MessageBuilder
            .withPayload(linkUpdateDto)
            .setHeader(KafkaHeaders.TOPIC, kafkaTopicName)
            .build();
        kafkaProducer.send(message);
    }
}
