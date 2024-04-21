package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.dtos.stackoverflow.AnswerResponse;
import edu.java.scrapper.dtos.stackoverflow.AnswersResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionsResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Builder
@Slf4j
public class StackOverflowQuestionsClient implements StackOverflowClient {
    private final WebClient webClient;
    private final Retry retryInstance;

    @Override
    public Optional<QuestionResponse> fetchData(String id) {
        try {
            return Optional.of(Objects.requireNonNull(this.webClient
                    .get()
                    .uri("/questions/{id}?site=stackoverflow", id)
                    .retrieve()
                    .bodyToMono(QuestionsResponse.class)
                    .retryWhen(retryInstance)
                    .block())
                .questionResponseList()
                .getFirst());
        } catch (Exception e) {
            log.info("Exception in StackOverflowClient#fetchData: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<AnswerResponse> fetchAnswers(String id) {
        try {
            return Objects.requireNonNull(this.webClient
                    .get()
                    .uri("/questions/{id}/answers?site=stackoverflow", id)
                    .retrieve()
                    .bodyToMono(AnswersResponse.class)
                    .retryWhen(retryInstance)
                    .block())
                .answersResponses();
        } catch (Exception e) {
            log.info("Exception in StackOverflowClient#fetchAnswers: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
