package edu.java.scrapper.integrationTests.services.jdbc;

import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class JdbcChatServiceTest extends IntegrationTest {
    private final JdbcChatService chatService;
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcChatServiceTest(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.chatService = new JdbcChatService(chatRepository, chatLinkRepository, linkRepository);
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
