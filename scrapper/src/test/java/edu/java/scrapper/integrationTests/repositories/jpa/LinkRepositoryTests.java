package edu.java.scrapper.integrationTests.repositories.jpa;

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
import java.util.Optional;

@Testcontainers
@SpringBootTest
public class LinkRepositoryTests extends IntegrationTest {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Autowired
    public LinkRepositoryTests(JpaChatRepository chatRepository, JpaLinkRepository linkRepository) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlAndId() {
        Link link = new Link();
        link.setUrl("test");
        link.setChatList(new ArrayList<>());
        link.setUpdatedAt(OffsetDateTime.now());
        link.setLastCheckedAt(OffsetDateTime.now());
        link.setLastCommitSha("commit");
        link.setAnswersCount(0L);
        linkRepository.save(link);

        Optional<Link> linkOptional = linkRepository.findByUrl("test");
        Assertions.assertTrue(linkOptional.isPresent());
        Assertions.assertTrue(linkRepository.findById(linkOptional.get().getId()).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void findAll() {
        Link link = new Link();
        link.setUrl("test");
        link.setChatList(new ArrayList<>());
        link.setUpdatedAt(OffsetDateTime.now());
        link.setLastCheckedAt(OffsetDateTime.now());
        link.setLastCommitSha("commit");
        link.setAnswersCount(0L);
        linkRepository.save(link);

        Link link2 = new Link();
        link2.setUrl("testik");
        link2.setChatList(new ArrayList<>());
        link2.setUpdatedAt(OffsetDateTime.now());
        link2.setLastCheckedAt(OffsetDateTime.now());
        link2.setLastCommitSha("commit");
        link2.setAnswersCount(0L);
        linkRepository.save(link2);

        List<Link> links = linkRepository.findAll();
        Assertions.assertEquals(2, links.size());
        Assertions.assertEquals(links.get(0).getUrl(), "test");
        Assertions.assertEquals(links.get(1).getUrl(), "testik");
    }
}
