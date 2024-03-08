package edu.java.scrapper.configuration;

import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Bean
    public GitHubReposClient gitHubReposClient() {
        return new GitHubReposClient();
    }

    @Bean
    public StackOverflowQuestionsClient stackOverflowQuestionsClient() {
        return new StackOverflowQuestionsClient();
    }
}
