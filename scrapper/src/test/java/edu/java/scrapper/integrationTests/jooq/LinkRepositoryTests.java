package edu.java.scrapper.integrationTests.jooq;

import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
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
public class LinkRepositoryTests extends IntegrationTest {
    private final JooqLinkRepository linkRepository;

    @Autowired
    public LinkRepositoryTests(JooqLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        String url = "randomUrl";
        Link link = new Link(0, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(link);
        Assertions.assertTrue(linkRepository.findByUrl(url).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {
        String url = "randomUrl";
        Link link = new Link(0, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(link);
        Assertions.assertTrue(linkRepository.findByUrl(url).isPresent());
        linkRepository.delete(url);
        Assertions.assertTrue(linkRepository.findByUrl(url).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void findAllLinksTest() {
        String url = "randomUrl";
        String url2 = "randomUrl2";
        Link link = new Link(0, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        Link link2 = new Link(0, url2, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(link);
        linkRepository.add(link2);
        List<Link> linkList = linkRepository.findAll();
        Assertions.assertEquals(List.of(url, url2), List.of(linkList.get(0).getUrl(), linkList.get(1).getUrl()));
    }
}
