package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

public abstract class TestsMockitoInitializer {
    public static Update update;
    public static Message message;
    public static Chat chat;

    @BeforeAll
    static void initialize() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }
}
