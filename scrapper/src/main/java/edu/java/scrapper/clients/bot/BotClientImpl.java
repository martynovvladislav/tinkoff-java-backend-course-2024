package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdateDto;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClientImpl implements BotClient {
    private static final String DEFAULT_URL = "http://localhost:8090";
    private static final String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;

    public BotClientImpl() {
        this(DEFAULT_URL);
    }

    public BotClientImpl(String url) {
        this.webClient = WebClient
            .builder()
            .baseUrl(url)
            .build();
    }

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
