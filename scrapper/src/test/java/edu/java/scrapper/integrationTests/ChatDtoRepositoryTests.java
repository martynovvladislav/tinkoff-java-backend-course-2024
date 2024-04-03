package edu.java.scrapper.integrationTests;

import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.dtos.ChatDto;
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
public class ChatDtoRepositoryTests extends IntegrationTest {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatDtoRepositoryTests(ChatRepository chatRepository) {
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
        List<ChatDto> chatDtos = chatRepository.findAll();
        Assertions.assertEquals(chatDtos, List.of(new ChatDto(666L), new ChatDto(1337L)));
    }
}
