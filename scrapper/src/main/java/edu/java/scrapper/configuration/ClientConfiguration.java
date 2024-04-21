package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.bot.BotClientImpl;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import edu.java.scrapper.utils.LinearRetryBackoffSpec;
import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

@Configuration
public class ClientConfiguration {
    public static final String EXPONENTIAL_STRING = "exponential";
    public static final String LINEAR_STRING = "linear";
    private final WebClientConfiguration webClientConfiguration;

    @Autowired
    public ClientConfiguration(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    @Bean
    public GitHubReposClient gitHubReposClient() {
        return GitHubReposClient.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.githubClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(getRetryForClient(webClientConfiguration.githubClientProperties()))
            .build();
    }

    @Bean
    public StackOverflowQuestionsClient stackOverflowQuestionsClient() {
        return StackOverflowQuestionsClient.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.soClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(getRetryForClient(webClientConfiguration.soClientProperties()))
            .build();
    }

    @Bean
    public BotClientImpl botClient() {
        return BotClientImpl.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.botClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(getRetryForClient(webClientConfiguration.botClientProperties()))
            .build();
    }

    private Predicate<? super Throwable> buildFilter(List<Integer> retryCodes) {
        return e -> e instanceof WebClientResponseException
            && retryCodes.contains(((WebClientResponseException) e).getStatusCode().value());
    }

    private Retry getRetryForClient(WebClientProperties webClientProperties) {
        return switch (webClientProperties.getRetryPolicy()) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                webClientProperties.getRetryAttempts(),
                Duration.ofSeconds(webClientProperties.getRetryDuration())
            ).filter(buildFilter(webClientProperties.getRetryCodes()));
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                webClientProperties.getRetryAttempts(),
                Duration.ofSeconds(webClientProperties.getRetryDuration())
            ).filter(buildFilter(webClientProperties.getRetryCodes()));
            default -> Retry.fixedDelay(
                webClientProperties.getRetryAttempts(),
                Duration.ofSeconds(webClientProperties.getRetryDuration())
            ).filter(buildFilter(webClientProperties.getRetryCodes()));
        };
    }
}
