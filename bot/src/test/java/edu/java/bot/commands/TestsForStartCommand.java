package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsForStartCommand {
    public static Update update;
    public static Message message;
    public static Chat chat;

    @Autowired
    public StartCommand startCommand;

    @BeforeAll
    static void initialize() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        chat = Mockito.mock(Chat.class);
    }

    @Test
    @DisplayName("start command handle test")
    void handleTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);
        Mockito.when(chat.firstName()).thenReturn("TestUser");
        SendMessage sendMessage = startCommand.handle(update);
        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "Hello, " + "TestUser!\n" +
                "Welcome to the Link Listener Bot. This bot can help you to track updates on the web links\n" +
                "Use /help to see available commands"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
