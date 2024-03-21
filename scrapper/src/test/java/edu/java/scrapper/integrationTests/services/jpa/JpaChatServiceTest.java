package edu.java.scrapper.integrationTests.services.jpa;

import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.jpa.JpaChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class JpaChatServiceTest extends IntegrationTest {
    private final JpaChatService chatService;
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public JpaChatServiceTest(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatService = new JpaChatService(chatRepository, linkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        chatService.register(1L);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeChatTest() {
        chatService.register(1L);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isPresent());
        chatService.unregister(1L);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isEmpty());
    }
}
