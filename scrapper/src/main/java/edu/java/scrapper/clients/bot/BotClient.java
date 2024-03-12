package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdateDto;

public interface BotClient {
    void sendMessage(LinkUpdateDto linkUpdateDto);
}
