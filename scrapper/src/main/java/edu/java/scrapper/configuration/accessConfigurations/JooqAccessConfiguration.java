package edu.java.scrapper.configuration.accessConfigurations;

import edu.java.scrapper.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqChatRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.jooq.JooqChatService;
import edu.java.scrapper.services.jooq.JooqLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository
    ) {
        return new JooqLinkService(linkRepository, chatLinkRepository);
    }

    @Bean
    public ChatService chatService(
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository,
        JooqChatRepository chatRepository
    ) {
        return new JooqChatService(chatRepository, chatLinkRepository, linkRepository);
    }
}
