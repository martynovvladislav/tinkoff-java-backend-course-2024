package edu.java.bot.clients;

import edu.java.bot.dtos.AddLinkRequestDto;
import edu.java.bot.dtos.LinkResponseDto;
import edu.java.bot.dtos.ListLinkResponseDto;
import edu.java.bot.dtos.RemoveLinkRequestDto;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Builder
@Slf4j
public class ScrapperClientImpl implements ScrapperClient {
    private static final String LINKS_ENDPOINT = "/links";
    private static final String TG_ENDPOINT = "/tg-chat/{id}";
    private static final String TG_HEADER = "Tg-chat-Id";
    private final WebClient webClient;
    private final Retry retryInstance;

    @Override
    public ListLinkResponseDto getLinks(Long tgChatId) {
        try {
            return webClient.get()
                .uri(LINKS_ENDPOINT)
                .header(TG_HEADER, String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(ListLinkResponseDto.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Exception in ScrapperClient#getLinks: " + e.getMessage());
            if (e instanceof WebClientResponseException) {
                throw (WebClientResponseException) e;
            } else {
                throw e;
            }
        }
    }

    @Override
    public LinkResponseDto addLink(Long tgChatId, AddLinkRequestDto addLinkRequestDto) {
        try {
            return webClient.post()
                .uri(LINKS_ENDPOINT)
                .header(TG_HEADER, String.valueOf(tgChatId))
                .bodyValue(addLinkRequestDto)
                .retrieve()
                .bodyToMono(LinkResponseDto.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Exception in ScrapperClient#addLink: " + e.getMessage());
            if (e instanceof WebClientResponseException) {
                throw (WebClientResponseException) e;
            } else {
                throw e;
            }
        }
    }

    @Override
    public LinkResponseDto deleteLink(Long tgChatId, RemoveLinkRequestDto removeLinkRequestDto) {
        try {
            return webClient.method(HttpMethod.DELETE)
                .uri(LINKS_ENDPOINT)
                .header(TG_HEADER, String.valueOf(tgChatId))
                .bodyValue(removeLinkRequestDto)
                .retrieve()
                .bodyToMono(LinkResponseDto.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Exception in ScrapperClient#deleteLink: " + e.getMessage());
            if (e instanceof WebClientResponseException) {
                throw (WebClientResponseException) e;
            } else {
                throw e;
            }
        }
    }

    @Override
    public void registerChat(Long id) {
        try {
            webClient.post()
                .uri(TG_ENDPOINT, id)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Exception in ScrapperClient#registerChat: " + e.getMessage());
            if (e instanceof WebClientResponseException) {
                throw (WebClientResponseException) e;
            } else {
                throw e;
            }
        }
    }

    @Override
    public void deleteChat(Long id) {
        try {
            webClient.delete()
                .uri(TG_ENDPOINT, id)
                .retrieve()
                .bodyToMono(Void.class)
                .retryWhen(retryInstance)
                .block();
        } catch (Exception e) {
            log.info("Exception in ScrapperClient#deleteChat: " + e.getMessage());
            if (e instanceof WebClientResponseException) {
                throw (WebClientResponseException) e;
            } else {
                throw e;
            }
        }
    }
}
