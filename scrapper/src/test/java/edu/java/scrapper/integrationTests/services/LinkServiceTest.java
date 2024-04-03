package edu.java.scrapper.integrationTests.services;

import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.integrationTests.IntegrationTest;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.services.LinkService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.net.URI;
import java.net.URISyntaxException;

@Testcontainers
@SpringBootTest
public class LinkServiceTest extends IntegrationTest {
    private final ChatService chatService;
    private final LinkService linkService;
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Autowired
    public LinkServiceTest(
        ChatService chatService,
        LinkService linkService,
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        this.chatService = chatService;
        this.linkService = linkService;
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
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
}
