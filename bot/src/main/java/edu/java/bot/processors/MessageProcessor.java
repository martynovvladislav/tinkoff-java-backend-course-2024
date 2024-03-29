package edu.java.bot.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageProcessor implements UserMessageProcessor {

    private final List<Command> commands;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
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
