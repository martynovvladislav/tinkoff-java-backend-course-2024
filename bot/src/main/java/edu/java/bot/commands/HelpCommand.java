package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HelpCommand implements Command {

    private final List<? extends Command> commands;

    public HelpCommand(List<? extends Command> commands) {
        this.commands = commands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Showing available commands";
    }

    @Override
    public SendMessage handle(Update update) {
        String answer = commands
            .stream()
            .filter(cmd -> !cmd.command().equals("/start"))
            .map(
                cmd -> cmd.command() + " - " + cmd.description() + "\n"
            ).collect(Collectors.joining());
        return new SendMessage(
            update.message().chat().id(),
            "Available commands:\n" + answer
        );
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().equals(command());
    }
}
