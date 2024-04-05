package edu.java.scrapper.services;

import edu.java.scrapper.clients.bot.BotClientImpl;
import edu.java.scrapper.configuration.ApplicationConfig;
import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSender {
    private final BotClientImpl botClient;
    private final ApplicationConfig applicationConfig;
    private final ScrapperQueueProducer scrapperQueueProducer;

    public void send(LinkUpdateDto linkUpdateDto) {
        if (applicationConfig.useQueue()) {
            scrapperQueueProducer.send(linkUpdateDto, applicationConfig.kafkaTopicName());
        } else {
            botClient.sendMessage(linkUpdateDto);
        }
    }
}
