package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.dtos.ApiErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
        private final ScrapperClientImpl scrapperClient;

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

        try {
            scrapperClient.registerChat(message.chat().id());
        } catch (WebClientResponseException e) {
            ApiErrorResponseDto apiErrorResponse = e.getResponseBodyAs(ApiErrorResponseDto.class);
            sendMessage = new SendMessage(
                message.chat().id(),
                apiErrorResponse.getDescription()
            );
        }

        return sendMessage;
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().equals(command());
    }

}
