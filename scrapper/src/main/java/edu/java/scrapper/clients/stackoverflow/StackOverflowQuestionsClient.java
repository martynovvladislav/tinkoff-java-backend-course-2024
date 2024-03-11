package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionsResponse;
import java.util.Objects;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowQuestionsClient implements StackOverflowClient {
    private final WebClient webClient;
    private final WebClientConfiguration webClientConfiguration;

    public StackOverflowQuestionsClient(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
        this.webClient = WebClient.builder()
            .baseUrl(this.webClientConfiguration.soClientConfig().baseUrl())
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
