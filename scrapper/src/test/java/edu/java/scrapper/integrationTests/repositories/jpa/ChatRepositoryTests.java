package edu.java.scrapper.integrationTests.repositories.jpa;

import edu.java.scrapper.domain.jpa.dtos.Chat;
import edu.java.scrapper.domain.jpa.dtos.Link;
import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest
public class ChatRepositoryTests extends IntegrationTest {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public ChatRepositoryTests(JpaChatRepository chatRepository, JpaLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void findByChatId() {
        Chat chat = new Chat();
        chat.setLinkList(new ArrayList<>());
        chat.setChatId(1L);
        chatRepository.save(chat);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteByChatId() {
        Chat chat = new Chat();
        chat.setLinkList(new ArrayList<>());
        chat.setChatId(1L);
        chatRepository.save(chat);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isPresent());
        chatRepository.deleteByChatId(1L);
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllWithConcreteLinkTracking() {
        Link link = new Link();
        link.setUrl("test");
        link.setChatList(new ArrayList<>());
        link.setUpdatedAt(OffsetDateTime.now());
        link.setLastCheckedAt(OffsetDateTime.now());
        link.setLastCommitSha("commit");
        link.setAnswersCount(0L);
        linkRepository.save(link);

        Chat chat = new Chat();
        chat.setLinkList(new ArrayList<>());
        chat.setChatId(1L);
        link.addChat(chat);
        chatRepository.save(chat);

        Chat chat1 = new Chat();
        chat1.setLinkList(new ArrayList<>());
        chat1.setChatId(2L);
        link.addChat(chat1);
        chatRepository.save(chat1);
        List<Chat> chats = chatRepository.findAllByLinkListContaining(link);
        System.out.println(chats);
    }
}
