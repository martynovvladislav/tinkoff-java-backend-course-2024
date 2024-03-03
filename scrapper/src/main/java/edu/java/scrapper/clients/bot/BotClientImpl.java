package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdate;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClientImpl implements BotClient {
    private final static String DEFAULT_URL = "http://localhost:8090";
    private final static String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;

    public BotClientImpl() {
        this(DEFAULT_URL);
    }

    public BotClientImpl(String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public void sendMessage(LinkUpdate linkUpdate) {
        webClient.post()
            .uri(UPDATES_ENDPOINT)
            .bodyValue(linkUpdate)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
