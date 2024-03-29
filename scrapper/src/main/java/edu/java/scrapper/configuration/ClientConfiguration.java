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
import reactor.util.retry.RetryBackoffSpec;

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
        Retry retry = switch (webClientConfiguration.githubClientProperties().getRetryPolicy()) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                webClientConfiguration.githubClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.githubClientProperties().getRetryDuration())
            );
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                webClientConfiguration.githubClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.githubClientProperties().getRetryDuration())
            );
            default -> Retry.fixedDelay(
                webClientConfiguration.githubClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.githubClientProperties().getRetryDuration())
            );
        };

        if (retry instanceof RetryBackoffSpec) {
            retry = ((RetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.githubClientProperties().retryCodes));
        } else {
            retry = ((LinearRetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.githubClientProperties().retryCodes));
        }

        return GitHubReposClient.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.githubClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(retry)
            .build();
    }

    @Bean
    public StackOverflowQuestionsClient stackOverflowQuestionsClient() {
        Retry retry = switch (webClientConfiguration.soClientProperties().getRetryPolicy()) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                webClientConfiguration.soClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.soClientProperties().getRetryDuration())
            );
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                webClientConfiguration.soClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.soClientProperties().getRetryDuration())
            );
            default -> Retry.fixedDelay(
                webClientConfiguration.soClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.soClientProperties().getRetryDuration())
            );
        };

        if (retry instanceof RetryBackoffSpec) {
            retry = ((RetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.soClientProperties().retryCodes));
        } else {
            retry = ((LinearRetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.soClientProperties().retryCodes));
        }

        return StackOverflowQuestionsClient.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.soClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(retry)
            .build();
    }

    @Bean
    public BotClientImpl botClient() {
        Retry retry = switch (webClientConfiguration.botClientProperties().getRetryPolicy()) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                webClientConfiguration.botClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.botClientProperties().getRetryDuration())
            );
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                webClientConfiguration.botClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.botClientProperties().getRetryDuration())
            );
            default -> Retry.fixedDelay(
                webClientConfiguration.botClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.botClientProperties().getRetryDuration())
            );
        };

        if (retry instanceof RetryBackoffSpec) {
            retry = ((RetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.botClientProperties().retryCodes));
        } else {
            retry = ((LinearRetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.botClientProperties().retryCodes));
        }

        return BotClientImpl.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.botClientProperties().getBaseUrl())
                    .build()
            )
            .retryInstance(retry)
            .build();
    }

    private Predicate<? super Throwable> buildFilter(List<Integer> retryCodes) {
        return e -> e instanceof WebClientResponseException
            && retryCodes.contains(((WebClientResponseException) e).getStatusCode().value());
    }
}
