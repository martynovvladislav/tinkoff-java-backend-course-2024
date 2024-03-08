package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class ListCommand implements Command {

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Showing the list of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(
            update.message().chat().id(),
            "No links are being tracked currently"
        );
    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }
}
