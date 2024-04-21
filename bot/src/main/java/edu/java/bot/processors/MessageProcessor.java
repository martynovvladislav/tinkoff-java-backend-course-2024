package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {

    private final List<Command> commands;
    private final ScrapperClientImpl scrapperClient;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        if (update.message() == null) {
            if (!update.myChatMember().oldChatMember().status()
                .equals(update.myChatMember().newChatMember().status())) {
                try {
                    scrapperClient.deleteChat(update.myChatMember().chat().id());
                } catch (WebClientResponseException ignored) {

                }
                return new SendMessage(
                    update.myChatMember().chat().id(),
                    "You left the chat"
                );
            } else {
                return new SendMessage(
                    update.myChatMember().chat().id(),
                    "No text message was found"
                );
            }
        }
        Command command = commands()
            .stream()
            .filter(cmd -> cmd.supports(update))
            .findFirst()
            .orElse(null);
        if (command == null) {
            return new SendMessage(
                update.message().chat().id(),
                "Wrong command. Try again"
            );
        }
        return command.handle(update);
    }
}
