package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class TestsMockitoForUntrackCommand extends TestsMockitoInitializer {
    public UntrackCommand untrackCommand = Mockito.mock(UntrackCommand.class);

    @Test
    @DisplayName("track command handle test")
    void handleTest() {
        Mockito.when(untrackCommand.handle(update)).thenReturn(new SendMessage(
            11L,
            "Format: '/untrack your_link'"
        ));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/untrack");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);

        SendMessage sendMessage = untrackCommand.handle(update);

        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "Format: '/untrack your_link'"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
