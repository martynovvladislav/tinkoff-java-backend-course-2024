package edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "web-clients", ignoreUnknownFields = false)
public record WebClientConfiguration(
    @Bean
    GithubClientConfig githubClientConfig,

    @Bean
    SOClientConfig soClientConfig,

    @Bean
    BotClientConfig botClientConfig

) {
    public record GithubClientConfig(String baseUrl) {}

    public record SOClientConfig(String baseUrl) {}

    public record BotClientConfig(String baseUrl) {}
}
