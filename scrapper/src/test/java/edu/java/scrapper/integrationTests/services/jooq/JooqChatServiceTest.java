package edu.java.scrapper.integrationTests.services.jooq;

import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqChatRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import edu.java.scrapper.services.jooq.JooqChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest("spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration")
public class JooqChatServiceTest extends IntegrationTest {
    private final JooqChatService chatService;
    private final JooqChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;
    private final JooqLinkRepository linkRepository;

    @Autowired
    public JooqChatServiceTest(
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository,
        JooqChatLinkRepository chatLinkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.chatService = new JooqChatService(chatRepository, chatLinkRepository, linkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        chatService.register(1L);
        Assertions.assertTrue(chatRepository.find(1L).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        chatService.register(1L);
        Assertions.assertTrue(chatRepository.find(1L).isPresent());
        chatService.unregister(1L);
        Assertions.assertTrue(chatRepository.find(1L).isEmpty());
    }
}
