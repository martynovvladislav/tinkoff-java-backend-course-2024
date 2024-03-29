package edu.java.bot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "web-clients", ignoreUnknownFields = false)
public record WebClientConfiguration(
    @Bean
    WebClientProperties scrapperClientProperties
) {}
