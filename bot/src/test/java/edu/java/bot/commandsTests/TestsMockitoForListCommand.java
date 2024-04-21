package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.ListCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

public class TestsMockitoForListCommand extends TestsMockitoInitializer {
    public ListCommand listCommand = Mockito.mock(ListCommand.class);

    @Test
    @DisplayName("list command handle test")
    void handleTest() {
        Mockito.when(listCommand.handle(update)).thenReturn(new SendMessage(
            11L,
            "No links are being tracked currently"
        ));
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
