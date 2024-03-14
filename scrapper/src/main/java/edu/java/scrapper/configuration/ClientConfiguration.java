package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.bot.BotClientImpl;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfiguration {
    private WebClientConfiguration webClientConfiguration;

    @Autowired
    public ClientConfiguration(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    @Bean
    public GitHubReposClient gitHubReposClient() {
        return GitHubReposClient.builder()
            .webClient(
                WebClient.builder()
                .baseUrl(webClientConfiguration.githubClientConfig().getBaseUrl())
                .build()
            )
            .build();
    }

    @Bean
    public StackOverflowQuestionsClient stackOverflowQuestionsClient() {
        return StackOverflowQuestionsClient.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.soClientConfig().getBaseUrl())
                    .build()
            )
            .build();
    }

    @Bean
    public BotClientImpl botClient() {
        return BotClientImpl.builder()
            .webClient(
                WebClient.builder()
                    .baseUrl(webClientConfiguration.botClientConfig().getBaseUrl())
                    .build()
            )
            .build();
    }
}
