package edu.java.scrapper.integrationTests;

import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.domain.repositories.LinkRepository;
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
public class LinkDtoRepositoryTests extends IntegrationTest {
    private final LinkRepository linkRepository;

    @Autowired
    public LinkDtoRepositoryTests(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    @Transactional
    @Rollback
    void addLinkTest() {
        String url = "randomUrl";
        LinkDto linkDto = new LinkDto(null, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(linkDto);
        Assertions.assertTrue(linkRepository.findByUrl(url).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void deleteLinkTest() {
        String url = "randomUrl";
        LinkDto linkDto = new LinkDto(null, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(linkDto);
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
        LinkDto linkDto = new LinkDto(null, url, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        LinkDto linkDto2 = new LinkDto(null, url2, OffsetDateTime.now(), OffsetDateTime.now(), null, null);
        linkRepository.add(linkDto);
        linkRepository.add(linkDto2);
        List<LinkDto> linkDtoList = linkRepository.findAll();
        Assertions.assertEquals(List.of(url, url2), List.of(linkDtoList.get(0).getUrl(), linkDtoList.get(1).getUrl()));
    }
}
