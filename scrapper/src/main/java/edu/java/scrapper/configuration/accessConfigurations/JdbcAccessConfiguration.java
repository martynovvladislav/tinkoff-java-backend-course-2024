package edu.java.scrapper.configuration.accessConfigurations;

import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfiguration {
    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        return new JdbcLinkService(linkRepository, chatLinkRepository);
    }

    @Bean
    public ChatService chatService(
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository,
        JdbcChatRepository chatRepository
    ) {
        return new JdbcChatService(chatRepository, chatLinkRepository, linkRepository);
    }
}
