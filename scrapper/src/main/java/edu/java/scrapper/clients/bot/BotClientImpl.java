package edu.java.scrapper.clients.bot;

import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.LinkUpdateDto;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClientImpl implements BotClient {
    private static final String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;
    private final WebClientConfiguration webClientConfiguration;

    public BotClientImpl(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
        this.webClient = WebClient
            .builder()
            .baseUrl(this.webClientConfiguration.botClientConfig().baseUrl())
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
