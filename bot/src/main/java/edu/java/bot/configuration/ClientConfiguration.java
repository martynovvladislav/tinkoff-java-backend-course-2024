package edu.java.bot.configuration;

import edu.java.bot.clients.ScrapperClientImpl;
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
    public ScrapperClientImpl scrapperClient() {
        return new ScrapperClientImpl(webClientConfiguration);
    }
}
