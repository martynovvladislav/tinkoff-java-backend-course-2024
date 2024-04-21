package edu.java.scrapper.configuration.accessConfigurations;

import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.jpa.JpaChatService;
import edu.java.scrapper.services.jpa.JpaLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository
    ) {
        return new JpaLinkService(chatRepository, linkRepository);
    }

    @Bean
    public ChatService chatService(
        JpaLinkRepository linkRepository,
        JpaChatRepository chatRepository
    ) {
        return new JpaChatService(chatRepository, linkRepository);
    }
}
