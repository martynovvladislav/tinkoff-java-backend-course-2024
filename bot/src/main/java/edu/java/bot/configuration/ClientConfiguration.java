package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperClientImpl;
import edu.java.bot.clients.customRetryBackoffSpecs.LinearRetryBackoffSpec;
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
    public ScrapperClientImpl scrapperClient() {
        Retry retry = switch (webClientConfiguration.scrapperClientProperties().getRetryPolicy()) {
            case EXPONENTIAL_STRING -> Retry.backoff(
                webClientConfiguration.scrapperClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.scrapperClientProperties().getRetryDuration())
            );
            case LINEAR_STRING -> LinearRetryBackoffSpec.linearBackoff(
                webClientConfiguration.scrapperClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.scrapperClientProperties().getRetryDuration())
            );
            default -> Retry.fixedDelay(
                webClientConfiguration.scrapperClientProperties().getRetryAttempts(),
                Duration.ofSeconds(webClientConfiguration.scrapperClientProperties().getRetryDuration())
            );
        };

        if (retry instanceof RetryBackoffSpec) {
            retry = ((RetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.scrapperClientProperties().retryCodes));
        } else {
            retry = ((LinearRetryBackoffSpec) retry)
                .filter(buildFilter(webClientConfiguration.scrapperClientProperties().retryCodes));
        }

        return ScrapperClientImpl.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.scrapperClientProperties().getBaseUrl())
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
