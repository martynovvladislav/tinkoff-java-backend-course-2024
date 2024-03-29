package edu.java.scrapper.clients.bot;

import edu.java.scrapper.dtos.LinkUpdateDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Builder
@Slf4j
public class BotClientImpl implements BotClient {
    private static final String UPDATES_ENDPOINT = "/updates";
    private final WebClient webClient;
    private final Retry retryInstance;

    @Override
    public void sendMessage(LinkUpdateDto linkUpdateDto) {
        try {
            webClient.post()
                .uri(UPDATES_ENDPOINT)
                .bodyValue(linkUpdateDto)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Error in BotClient: " + e.getMessage());
        }
    }
}
