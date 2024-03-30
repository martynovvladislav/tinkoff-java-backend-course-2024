package edu.java.scrapper.integrationTests.jdbc;

import edu.java.scrapper.domain.dtos.ChatLinkDto;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import java.time.OffsetDateTime;
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
public class ChatLinkRepositoryTests extends IntegrationTest {
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public ChatLinkRepositoryTests(
        JdbcChatLinkRepository chatLinkRepository,
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository
    ) {
        this.chatLinkRepository = chatLinkRepository;
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addChatLinkTest() {
        Long tgChatId = 1L;
        chatRepository.add(tgChatId);
        linkRepository.add(new LinkDto(null, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
        Long linkId = linkRepository.getLinkId("test");

        chatLinkRepository.add(tgChatId, linkId);
        Assertions.assertTrue(chatLinkRepository.find(tgChatId, linkId).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteChatTest() {
        Long tgChatId = 1L;
        chatRepository.add(tgChatId);
        linkRepository.add(new LinkDto(null, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
        Long linkId = linkRepository.getLinkId("test");

        chatLinkRepository.add(tgChatId, linkId);
        Assertions.assertTrue(chatLinkRepository.find(tgChatId, linkId).isPresent());

        chatLinkRepository.delete(tgChatId, linkId);
        Assertions.assertTrue(chatLinkRepository.find(tgChatId, linkId).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllChatsTest() {
        Long tgChatId = 1L;
        chatRepository.add(tgChatId);
        linkRepository.add(new LinkDto(null, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
        Long linkId = linkRepository.getLinkId("test");

        Long tgChatId2 = 2L;
        chatRepository.add(tgChatId2);
        linkRepository.add(new LinkDto(null, "test2", OffsetDateTime.now(), OffsetDateTime.now(), null, null));

        chatLinkRepository.add(tgChatId, linkId);
        chatLinkRepository.add(tgChatId2, linkId);

        List<ChatLinkDto> chatLinkDtos = chatLinkRepository.findAll();
        Assertions.assertEquals(chatLinkDtos.get(0).getChatId(), tgChatId);
        Assertions.assertEquals(chatLinkDtos.get(1).getChatId(), tgChatId2);
    }
}
