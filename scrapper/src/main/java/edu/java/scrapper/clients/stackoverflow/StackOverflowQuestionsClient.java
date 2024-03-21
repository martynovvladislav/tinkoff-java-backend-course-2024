package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionsResponse;
import java.util.Objects;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;

@Builder
public class StackOverflowQuestionsClient implements StackOverflowClient {
    private final WebClient webClient;

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
