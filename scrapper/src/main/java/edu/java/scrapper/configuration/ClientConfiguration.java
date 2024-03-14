package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.bot.BotClientImpl;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    private WebClientConfiguration webClientConfiguration;

    @Autowired
    public ClientConfiguration(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    @Bean
    public GitHubReposClient gitHubReposClient() {
        return GitHubReposClient.builder().baseUrl(webClientConfiguration.githubClientConfig().baseUrl()).build();
    }

    @Bean
    public StackOverflowQuestionsClient stackOverflowQuestionsClient() {
        return new StackOverflowQuestionsClient(webClientConfiguration);
    }

    @Bean
    public BotClientImpl botClient() {
        return new BotClientImpl(webClientConfiguration);
    }
}
