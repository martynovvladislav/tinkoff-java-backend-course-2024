package edu.java.scrapper.clients.stackoverflow;

import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowQuestionsClient implements StackOverflowClient {
    private final WebClient webClient;
    private final static String DEFAULT_URL = "https://api.stackexchange.com";

    public StackOverflowQuestionsClient() {
        this.webClient = WebClient.builder()
            .baseUrl(DEFAULT_URL)
            .build();
    }

    public StackOverflowQuestionsClient(String url) {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    @Override
    public QuestionResponse fetchData(String id) {
        return Objects.requireNonNull(this.webClient
                .get()
                .uri("/questions/{id}?site=stackoverflow", id)
                .retrieve()
                .bodyToMono(QuestionsResponse.class)
                .block())
            .questionResponseList()
            .getFirst();
    }
}
