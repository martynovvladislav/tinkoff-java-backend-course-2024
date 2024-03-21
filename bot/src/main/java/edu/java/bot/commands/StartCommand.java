package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {

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
                + "\nWelcome to the Link Listener Bot. This bot can help you track updates on the web links\n"
                + "Use /help to see available commands"
        );
        return sendMessage;
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().equals(command());
    }

}
