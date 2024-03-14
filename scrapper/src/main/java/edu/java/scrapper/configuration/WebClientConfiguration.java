package edu.java.scrapper.configuration;

import edu.java.scrapper.configuration.webclientconfigs.BotClientConfig;
import edu.java.scrapper.configuration.webclientconfigs.GithubClientConfig;
import edu.java.scrapper.configuration.webclientconfigs.SOClientConfig;
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
) {}
