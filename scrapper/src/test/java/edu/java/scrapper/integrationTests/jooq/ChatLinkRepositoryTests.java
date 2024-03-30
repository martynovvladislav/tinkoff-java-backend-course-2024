package edu.java.scrapper.integrationTests.jooq;

import edu.java.scrapper.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqChatRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
import edu.java.scrapper.domain.jooq.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.tables.pojos.Link;
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
    private final JooqChatLinkRepository chatLinkRepository;
    private final JooqChatRepository chatRepository;
    private final JooqLinkRepository linkRepository;

    @Autowired
    public ChatLinkRepositoryTests(
        JooqChatLinkRepository chatLinkRepository,
        JooqChatRepository chatRepository,
        JooqLinkRepository linkRepository
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
        linkRepository.add(new Link(0, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
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
        linkRepository.add(new Link(0, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
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
        linkRepository.add(new Link(0, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));
        Long linkId = linkRepository.getLinkId("test");

        Long tgChatId2 = 2L;
        chatRepository.add(tgChatId2);
        linkRepository.add(new Link(0, "test2", OffsetDateTime.now(), OffsetDateTime.now(), null, null));

        chatLinkRepository.add(tgChatId, linkId);
        chatLinkRepository.add(tgChatId2, linkId);

        List<ChatLink> chatLinks = chatLinkRepository.findAll();
        Assertions.assertEquals(chatLinks.get(0).getChatId(), tgChatId);
        Assertions.assertEquals(chatLinks.get(1).getChatId(), tgChatId2);
    }
}
