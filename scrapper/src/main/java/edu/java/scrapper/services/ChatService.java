package edu.java.scrapper.services;

public interface ChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);
}
