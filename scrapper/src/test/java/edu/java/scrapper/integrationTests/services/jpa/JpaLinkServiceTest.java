package edu.java.scrapper.integrationTests.services.jpa;

import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jpa.dtos.Chat;
import edu.java.scrapper.domain.jpa.dtos.Link;
import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.services.jpa.JpaChatService;
import edu.java.scrapper.services.jpa.JpaLinkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;

@Testcontainers
@SpringBootTest
public class JpaLinkServiceTest extends IntegrationTest {
    private final JpaChatService chatService;
    private final JpaLinkService linkService;
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public JpaLinkServiceTest(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatService = new JpaChatService(chatRepository, linkRepository);
        this.linkService = new JpaLinkService(chatRepository, linkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void addChatLink() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isPresent());
        Chat chat = chatRepository.findChatByChatId(1L).get();
        Assertions.assertTrue(linkRepository.findByUrl("test").get().chatList.contains(chat));
    }

    @Test
    @Transactional
    @Rollback
    void removeChatLink() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isPresent());
        Chat chat = chatRepository.findChatByChatId(1L).get();
        Assertions.assertTrue(linkRepository.findByUrl("test").get().chatList.contains(chat));
        linkService.remove(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isEmpty());
        Assertions.assertTrue(chat.linkList.isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void removeChat() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Chat chat = chatRepository.findChatByChatId(1L).get();
        Assertions.assertTrue(linkRepository.findByUrl("test").get().chatList.contains(chat));
        chatService.unregister(1L);
        Assertions.assertTrue(linkRepository.findByUrl("test").isEmpty());
        Assertions.assertTrue(chatRepository.findChatByChatId(1L).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void listAll() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        linkService.add(1L, new URI("testik"));
        List<LinkDto> linkDtos = linkService.listAll(1L);
        Assertions.assertEquals(linkDtos.get(0).getUrl(), "test");
        Assertions.assertEquals(linkDtos.get(1).getUrl(), "testik");
    }

    @Test
    @Transactional
    @Rollback
    void update() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        OffsetDateTime offsetDateTime = OffsetDateTime.now().minusDays(5);
        linkService.update(
            new LinkDto(
                0L,
                "test",
                offsetDateTime,
                offsetDateTime,
                "last_commit",
                0L
            )
        );
        List<LinkDto> links = linkService.findOld(1);
        Assertions.assertEquals(links.size(), 1);
        Assertions.assertEquals(
            links.get(0).getUpdatedAt().toZonedDateTime()
                .withZoneSameInstant(offsetDateTime.toZonedDateTime().getZone()),
            offsetDateTime.toZonedDateTime());
        Assertions.assertEquals(links.get(0).getLastCheckedAt().toZonedDateTime()
                .withZoneSameInstant(offsetDateTime.toZonedDateTime().getZone()),
            offsetDateTime.toZonedDateTime());
        Assertions.assertEquals(links.get(0).getLastCommitSha(), "last_commit");
        Assertions.assertEquals(links.get(0).getAnswersCount(), 0L);
    }

    @Test
    @Transactional
    @Rollback
    void listByLinkId() throws URISyntaxException {
        chatService.register(1L);
        chatService.register(2L);
        linkService.add(1L, new URI("test"));
        linkService.add(2L, new URI("test"));
        List<Long> chatIds = linkService.listAllByLinkId(linkRepository.findByUrl("test").get().getId());
        Assertions.assertEquals(chatIds.get(0), 1L);
        Assertions.assertEquals(chatIds.get(1), 2L);
    }
}
