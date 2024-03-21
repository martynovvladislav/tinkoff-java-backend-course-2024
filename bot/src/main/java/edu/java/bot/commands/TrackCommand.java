package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.dtos.AddLinkRequestDto;
import edu.java.bot.dtos.ApiErrorResponseDto;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final ScrapperClientImpl scrapperClient;

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
        if (update.message().text().split(" ").length == 1) {
            return new SendMessage(
                update.message().chat().id(),
                "Format: '/track your_link'"
            );
        }

        String url = update.message().text().split(" ")[1];
        try {
            scrapperClient.addLink(
                update.message().chat().id(),
                new AddLinkRequestDto(new URI(url))
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
            "Link has been added!"
        );
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().split(" ")[0].equals(command());
    }
}
