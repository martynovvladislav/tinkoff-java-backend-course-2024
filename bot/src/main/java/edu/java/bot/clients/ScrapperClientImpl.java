package edu.java.bot.clients;

import edu.java.bot.dtos.AddLinkRequest;
import edu.java.bot.dtos.LinkResponse;
import edu.java.bot.dtos.ListLinkResponse;
import edu.java.bot.dtos.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientImpl implements ScrapperClient {
    private final static String DEFAULT_URL = "http://localhost:8080";
    private final static String LINKS_ENDPOINT = "/links";
    private final static String TG_ENDPOINT = "/tg-chat/{id}";
    private final static String TG_HEADER = "Tg-chat-Id";
    private final WebClient webClient;

    public ScrapperClientImpl() {
        this(DEFAULT_URL);
    }

    public ScrapperClientImpl(String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public ListLinkResponse getLinks(Long tgChatId) {
        return webClient.get()
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(ListLinkResponse.class)
            .block();
    }

    @Override
    public LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest) {
        return webClient.post()
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Override
    public LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Override
    public void registerChat(Long id) {
        webClient.post()
            .uri(TG_ENDPOINT, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    @Override
    public void deleteChat(Long id) {
        webClient.delete()
            .uri(TG_ENDPOINT, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
