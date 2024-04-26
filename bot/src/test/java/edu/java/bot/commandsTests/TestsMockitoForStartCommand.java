package edu.java.bot.commandsTests;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.StartCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestsMockitoForStartCommand extends TestsMockitoInitializer {
    public StartCommand startCommand = Mockito.mock(StartCommand.class);

    @Test
    @DisplayName("start command handle test")
    void handleTest() {
        Mockito.when(startCommand.handle(update)).thenReturn(new SendMessage(
            11L,
            "Hello, " + "TestUser!\n"
        ));
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(11L);
        Mockito.when(chat.firstName()).thenReturn("TestUser");

        SendMessage sendMessage = startCommand.handle(update);

        Assertions.assertEquals(
            sendMessage.getParameters().get("text"),
            "Hello, " + "TestUser!\n"
        );
        Assertions.assertEquals(
            sendMessage.getParameters().get("chat_id"),
            11L
        );
    }
}
