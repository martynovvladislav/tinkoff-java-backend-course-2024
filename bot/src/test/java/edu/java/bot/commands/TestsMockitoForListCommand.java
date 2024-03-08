package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsMockitoForListCommand extends TestsMockitoInitializer {
    @Autowired
    public ListCommand listCommand;

    @Test
    @DisplayName("list command handle test")
    void handleTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);

        SendMessage sendMessage = listCommand.handle(update);

        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "No links are being tracked currently"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
