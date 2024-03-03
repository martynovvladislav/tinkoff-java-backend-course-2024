package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdate;

public interface BotClient {
    void sendMessage(LinkUpdate linkUpdate);
}
