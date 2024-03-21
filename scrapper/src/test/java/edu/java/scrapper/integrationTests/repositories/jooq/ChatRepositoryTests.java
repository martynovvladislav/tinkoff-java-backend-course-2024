package edu.java.scrapper.integrationTests.repositories.jooq;

import edu.java.scrapper.domain.jooq.repositories.JooqChatRepository;
import edu.java.scrapper.domain.jooq.tables.pojos.Chat;
import edu.java.scrapper.integrationTests.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class ChatRepositoryTests extends IntegrationTest {
    private final JooqChatRepository chatRepository;

    @Autowired
    public ChatRepositoryTests(JooqChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addChatTest() {
        Long tgChatId = 666L;
        chatRepository.add(tgChatId);
        Assertions.assertTrue(chatRepository.find(tgChatId).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        Long tgChatId = 666L;
        chatRepository.add(tgChatId);
        Assertions.assertNotNull(chatRepository.find(tgChatId));
        chatRepository.delete(tgChatId);
        Assertions.assertTrue(chatRepository.find(tgChatId).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsTest() {
        chatRepository.add(666L);
        chatRepository.add(1337L);
        List<Chat> chats = chatRepository.findAll();
        Assertions.assertEquals(2, chats.size());
    }
}
