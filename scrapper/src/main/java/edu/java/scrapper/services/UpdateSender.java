package edu.java.scrapper.services;

import edu.java.scrapper.clients.bot.BotClient;
import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateSender {
    private final BotClient botClient;

    public void send(LinkUpdateDto linkUpdateDto) {
        botClient.sendMessage(linkUpdateDto);
    }
}
