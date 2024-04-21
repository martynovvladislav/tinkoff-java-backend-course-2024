package edu.java.bot.suppliers;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.linklistenerbot.LinkListenerBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSupplier implements Supplier {
    private final LinkListenerBot bot;

    @Override
    public void send(Long tgChatId, String message) {
        bot.execute(
            new SendMessage(
                tgChatId,
                message
            )
        );
    }
}
