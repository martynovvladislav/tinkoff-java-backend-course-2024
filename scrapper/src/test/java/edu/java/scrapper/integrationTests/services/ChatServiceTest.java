package edu.java.scrapper.integrationTests.services;

import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class ChatServiceTest extends IntegrationTest {
    private final ChatService chatService;
    private final JdbcChatRepository chatRepository;

    @Autowired
    public ChatServiceTest(ChatService chatService, JdbcChatRepository chatRepository) {
        this.chatService = chatService;
        this.chatRepository = chatRepository;
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
