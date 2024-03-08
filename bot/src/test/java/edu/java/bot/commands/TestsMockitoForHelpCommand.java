package edu.java.bot.commands;

import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsMockitoForHelpCommand extends TestsMockitoInitializer {
    @Autowired
    public HelpCommand helpCommand;

    @Test
    @DisplayName("help command handle test")
    void handleTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/help");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);

        SendMessage sendMessage = helpCommand.handle(update);

        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "Available commands:\n" +
                "/list - Showing the list of tracked links\n" +
                "/track - Starting tracking a link\n" +
                "/untrack - Stopping tracking a link\n"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
