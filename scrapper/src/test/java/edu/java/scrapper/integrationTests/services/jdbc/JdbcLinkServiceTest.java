package edu.java.scrapper.integrationTests.services.jdbc;

import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import edu.java.scrapper.services.jdbc.JdbcChatService;
import edu.java.scrapper.services.jdbc.JdbcLinkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
public class JdbcLinkServiceTest extends IntegrationTest {
    private final JdbcChatService chatService;
    private final JdbcLinkService linkService;
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Autowired
    public JdbcLinkServiceTest(
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
        this.chatService = new JdbcChatService(chatRepository, chatLinkRepository, linkRepository);
        this.linkService = new JdbcLinkService(linkRepository, chatLinkRepository);
    }

    @Test
    @Transactional
    @Rollback
    void addChatLink() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isPresent());
        Assertions.assertTrue(chatLinkRepository.find(1L, linkRepository.getLinkId("test")).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void removeChatLink() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isPresent());
        Assertions.assertTrue(chatLinkRepository.find(1L, linkRepository.getLinkId("test")).isPresent());
        linkService.remove(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isEmpty());
        Assertions.assertTrue(chatLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void removeChat() throws URISyntaxException {
        chatService.register(1L);
        linkService.add(1L, new URI("test"));
        Assertions.assertTrue(linkRepository.findByUrl("test").isPresent());
        Assertions.assertTrue(chatLinkRepository.find(1L, linkRepository.getLinkId("test")).isPresent());
        chatService.unregister(1L);
        Assertions.assertTrue(linkRepository.findByUrl("test").isEmpty());
        Assertions.assertTrue(chatLinkRepository.findAll().isEmpty());
        Assertions.assertTrue(chatRepository.find(1L).isEmpty());
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
        LinkDto linkDto = linkRepository.findByUrl("test").get();
        linkDto.setUpdatedAt(offsetDateTime);
        linkDto.setLastCheckedAt(offsetDateTime);
        linkDto.setAnswersCount(0L);
        linkDto.setLastCommitSha("last_commit");
        linkService.update(linkDto);
        List<LinkDto> links = linkService.findOld(1);
        Assertions.assertEquals(links.size(), 1);
        Assertions.assertEquals(
            links.get(0).getUpdatedAt().toZonedDateTime()
                .withZoneSameInstant(offsetDateTime.toZonedDateTime().getZone()).withNano(0),
            offsetDateTime.toZonedDateTime().withNano(0));
        Assertions.assertEquals(links.get(0).getLastCheckedAt().toZonedDateTime()
                .withZoneSameInstant(offsetDateTime.toZonedDateTime().getZone()).withNano(0),
            offsetDateTime.toZonedDateTime().withNano(0));
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
