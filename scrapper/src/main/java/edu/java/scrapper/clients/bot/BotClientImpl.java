package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;

@Builder
public class BotClientImpl implements BotClient {
    private static final String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;

    @Override
    public void sendMessage(LinkUpdateDto linkUpdateDto) {
        webClient.post()
            .uri(UPDATES_ENDPOINT)
            .bodyValue(linkUpdateDto)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
