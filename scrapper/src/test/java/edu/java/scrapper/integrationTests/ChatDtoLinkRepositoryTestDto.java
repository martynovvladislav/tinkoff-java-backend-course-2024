package edu.java.scrapper.integrationTests;

import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.domain.dtos.ChatLinkDto;
import edu.java.scrapper.domain.dtos.LinkDto;
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
public class ChatDtoLinkRepositoryTestDto extends IntegrationTest {
    private final ChatLinkRepository chatLinkRepository;
    private final ChatRepository chatRepository;
    private final LinkRepository linkRepository;

    @Autowired
    public ChatDtoLinkRepositoryTestDto(
        ChatLinkRepository chatLinkRepository,
        ChatRepository chatRepository,
        LinkRepository linkRepository
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
        Integer linkId = linkRepository.getLinkId("test");

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
        Integer linkId = linkRepository.getLinkId("test");

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
        Integer linkId = linkRepository.getLinkId("test");

        Long tgChatId2 = 2L;
        chatRepository.add(tgChatId2);
        linkRepository.add(new LinkDto(null, "test", OffsetDateTime.now(), OffsetDateTime.now(), null, null));

        chatLinkRepository.add(tgChatId, linkId);
        chatLinkRepository.add(tgChatId2, linkId);

        List<ChatLinkDto> chatLinkDtos = chatLinkRepository.findAll();
        Assertions.assertEquals(chatLinkDtos.get(0).getChatId(), tgChatId);
        Assertions.assertEquals(chatLinkDtos.get(1).getChatId(), tgChatId2);
    }
}
