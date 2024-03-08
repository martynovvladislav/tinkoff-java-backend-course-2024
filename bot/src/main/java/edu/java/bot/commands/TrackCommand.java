package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Starting tracking a link";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "Currently in development"
        );
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().equals(command());
    }
}
