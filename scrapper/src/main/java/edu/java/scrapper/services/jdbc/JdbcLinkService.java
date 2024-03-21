package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.domain.jdbc.dtos.ChatLinkDto;
import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.exceptions.LinkAlreadyExistException;
import edu.java.scrapper.exceptions.LinkDoesNotExistException;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public void add(Long tgChatId, URI url) {
        String urlString = url.toString();
        LinkDto linkDto = new LinkDto(
            0L,
            urlString,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            "0",
            0L
        );
        if (linkRepository.findByUrl(urlString).isEmpty()) {
            linkRepository.add(linkDto);
        }
        Long linkId = linkRepository.getLinkId(urlString);
        if (chatLinkRepository.find(tgChatId, linkId).isPresent()) {
            throw new LinkAlreadyExistException();
        }

        chatLinkRepository.add(tgChatId, linkId);
    }

    @Override
    @Transactional
    public void remove(long tgChatId, URI url) {
        if (linkRepository.findByUrl(url.toString()).isEmpty()) {
            throw new LinkDoesNotExistException();
        }
        Long linkId = linkRepository.getLinkId(url.toString());
        if (chatLinkRepository.find(tgChatId, linkId).isEmpty()) {
            throw new LinkDoesNotExistException();
        }

        chatLinkRepository.delete(tgChatId, linkId);
        if (chatLinkRepository.findAll().stream()
            .filter(
                chatLinkDto -> chatLinkDto.getLinkId().equals(linkId)
            ).toList().isEmpty()) {
            linkRepository.delete(url.toString());
        }
    }

    @Override
    @Transactional
    public List<LinkDto> listAll(long tgChatId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getChatId().equals(tgChatId))
            .map(chatLinkDto -> linkRepository.findById(chatLinkDto.getLinkId()).get())
            .toList();
    }

    @Override
    @Transactional
    public List<Long> listAllByLinkId(Long linkId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getLinkId().equals(linkId))
            .map(ChatLinkDto::getChatId)
            .toList();
    }

    @Override
    @Transactional
    public List<LinkDto> findOld(long secondThreshold) {
        return linkRepository.findAll().stream()
            .filter(
                link -> {
                    OffsetDateTime current = OffsetDateTime.now();
                    OffsetDateTime lastCheckedTime = link.getLastCheckedAt();
                    long secondsPassed = ChronoUnit.SECONDS.between(lastCheckedTime, current);
                    return secondsPassed >= secondThreshold;
                }
            )
            .toList();
    }

    @Override
    public void update(LinkDto linkDto) {
        linkRepository.update(linkDto);
    }
}
