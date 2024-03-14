package edu.java.scrapper.integrationTests;

import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.dtos.Chat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;

@Testcontainers
@SpringBootTest
public class ChatRepositoryTests extends IntegrationTest {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatRepositoryTests(ChatRepository chatRepository) {
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
        Assertions.assertEquals(chats, List.of(new Chat(666L), new Chat(1337L)));
    }
}
