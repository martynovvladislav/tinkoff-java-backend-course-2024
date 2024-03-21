package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.dtos.ApiErrorResponseDto;
import edu.java.bot.dtos.RemoveLinkRequestDto;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private final ScrapperClientImpl scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Stopping tracking a link";
    }

    @Override
    public SendMessage handle(Update update) {
        if (update.message().text().split(" ").length == 1) {
            return new SendMessage(
                update.message().chat().id(),
                "Format: '/untrack your_link'"
            );
        }

        String url = update.message().text().split(" ")[1];
        try {
            scrapperClient.deleteLink(
                update.message().chat().id(),
                new RemoveLinkRequestDto(new URI(url))
            );
        } catch (URISyntaxException e) {
            return new SendMessage(
                update.message().chat().id(),
                "Bad link! Try again"
            );
        } catch (WebClientResponseException e) {
            return new SendMessage(
                update.message().chat().id(),
                e.getResponseBodyAs(ApiErrorResponseDto.class).getDescription()
            );
        }

        return new SendMessage(
            update.message().chat().id(),
            "Link has been removed!"
        );
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().split(" ")[0].equals(command());
    }
}
