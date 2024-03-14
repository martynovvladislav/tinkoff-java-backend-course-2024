package edu.java.scrapper.integrationTests;

import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.dtos.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.OffsetDateTime;
import java.util.List;

@Testcontainers
@SpringBootTest
public class LinkRepositoryTests extends IntegrationTest {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkRepositoryTests(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        String url = "randomUrl";
        Link link = new Link(null, url, OffsetDateTime.now(), OffsetDateTime.now());
        linkRepository.add(link);
        Assertions.assertTrue(linkRepository.findByUrl(url).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {
        String url = "randomUrl";
        Link link = new Link(null, url, OffsetDateTime.now(), OffsetDateTime.now());
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
        Link link = new Link(null, url, OffsetDateTime.now(), OffsetDateTime.now());
        Link link2 = new Link(null, url2, OffsetDateTime.now(), OffsetDateTime.now());
        linkRepository.add(link);
        linkRepository.add(link2);
        List<Link> linkList = linkRepository.findAll();
        Assertions.assertEquals(List.of(url, url2), List.of(linkList.get(0).getUrl(), linkList.get(1).getUrl()));
    }
}
