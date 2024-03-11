package edu.java.bot.clients;

import edu.java.bot.dtos.AddLinkRequestDto;
import edu.java.bot.dtos.LinkResponseDto;
import edu.java.bot.dtos.ListLinkResponseDto;
import edu.java.bot.dtos.RemoveLinkRequestDto;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public class ScrapperClientImpl implements ScrapperClient {
    private static final String DEFAULT_URL = "http://localhost:8080";
    private static final String LINKS_ENDPOINT = "/links";
    private static final String TG_ENDPOINT = "/tg-chat/{id}";
    private static final String TG_HEADER = "Tg-chat-Id";
    private final WebClient webClient;

    public ScrapperClientImpl() {
        this(DEFAULT_URL);
    }

    public ScrapperClientImpl(String url) {
        this.webClient = WebClient
            .builder()
            .baseUrl(url)
            .build();
    }

    @Override
    public ListLinkResponseDto getLinks(Long tgChatId) {
        return webClient.get()
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .retrieve()
            .bodyToMono(ListLinkResponseDto.class)
            .block();
    }

    @Override
    public LinkResponseDto addLink(Long tgChatId, AddLinkRequestDto addLinkRequestDto) {
        return webClient.post()
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .bodyValue(addLinkRequestDto)
            .retrieve()
            .bodyToMono(LinkResponseDto.class)
            .block();
    }

    @Override
    public LinkResponseDto deleteLink(Long tgChatId, RemoveLinkRequestDto removeLinkRequestDto) {
        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_ENDPOINT)
            .header(TG_HEADER, String.valueOf(tgChatId))
            .bodyValue(removeLinkRequestDto)
            .retrieve()
            .bodyToMono(LinkResponseDto.class)
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
