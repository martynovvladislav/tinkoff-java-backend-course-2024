package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.HelpCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

public class TestsMockitoForHelpCommand extends TestsMockitoInitializer {
    public HelpCommand helpCommand = Mockito.mock(HelpCommand.class);

    @Test
    @DisplayName("help command handle test")
    void handleTest() {
        Mockito.when(helpCommand.handle(update)).thenReturn(new SendMessage(
            11L,
            "Available commands:\n" +
                "/list - Showing the list of tracked links\n" +
                "/track - Starting tracking a link\n" +
                "/untrack - Stopping tracking a link\n"
        ));
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
