package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.dtos.ApiErrorResponseDto;
import edu.java.bot.dtos.ListLinkResponseDto;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private final ScrapperClientImpl scrapperClient;

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
        try {
            ListLinkResponseDto listLinkResponseDto = scrapperClient.getLinks(
                update.message().chat().id()
            );
            if (listLinkResponseDto.getLinks().isEmpty()) {
                return new SendMessage(
                    update.message().chat().id(),
                    "No links are being tracked currently"
                );
            }
            return new SendMessage(
                update.message().chat().id(),
                "Tracking links:\n"
                + listLinkResponseDto.getLinks().stream()
                    .map(linkResponseDto -> linkResponseDto.getUrl().toString())
                    .collect(Collectors.joining("\n\n"))
            );
        } catch (WebClientResponseException e) {
            String msg = e.getLocalizedMessage();
            if (!(e.getResponseBodyAs(ApiErrorResponseDto.class) == null)) {
                msg = e.getResponseBodyAs(ApiErrorResponseDto.class).getDescription();
            }
            return new SendMessage(
                update.message().chat().id(),
                msg
            );
        } catch (Exception e) {
            return new SendMessage(
                update.message().chat().id(),
                e.getLocalizedMessage()
            );
        }

    }

    @Override
    public boolean supports(Update update) {
        return Command.super.supports(update);
    }
}
