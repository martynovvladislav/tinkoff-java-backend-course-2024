package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.TrackCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsMockitoForTrackCommand extends TestsMockitoInitializer {
    @Autowired
    public TrackCommand trackCommand;

    @Test
    @DisplayName("track command handle test")
    void handleTest() {
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);

        SendMessage sendMessage = trackCommand.handle(update);

        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "Format: '/track your_link'"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
