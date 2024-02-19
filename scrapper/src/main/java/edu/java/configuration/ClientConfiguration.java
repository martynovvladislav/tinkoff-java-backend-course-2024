package edu.java.configuration;

import edu.java.github.GitHubReposClient;
import edu.java.stackoverflow.StackOverflowQuestionsClient;
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
