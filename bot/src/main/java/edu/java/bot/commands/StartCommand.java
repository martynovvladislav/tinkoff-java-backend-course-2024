package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

    private final List<Command> commands;

    @Autowired
    public StartCommand(
        HelpCommand helpCommand,
        TrackCommand trackCommand,
        UntrackCommand untrackCommand,
        ListCommand listCommand
    ) {
        commands = new ArrayList<>(
            List.of(
                helpCommand,
                trackCommand,
                untrackCommand,
                listCommand
            )
        );
    }

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Starting a bot";
    }

    @Override
    public SendMessage handle(Update update) {
        Message message = update.message();
        SendMessage sendMessage = new SendMessage(
            message.chat().id(),
            "Hello, " + message.chat().firstName() + "!"
                + "\nWelcome to the Link Listener Bot. This bot can help you to track updates on the web links\n"
                + "Use /help to see available commands"
        );
        return sendMessage;
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().equals(command());
    }

}
