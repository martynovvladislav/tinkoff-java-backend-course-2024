package edu.java.scrapper.services.jooq;

import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
import edu.java.scrapper.domain.jooq.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.tables.pojos.Link;
import edu.java.scrapper.exceptions.LinkAlreadyExistException;
import edu.java.scrapper.exceptions.LinkDoesNotExistException;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JooqLinkService implements LinkService {
    private final JooqLinkRepository linkRepository;
    private final JooqChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public void add(Long tgChatId, URI url) {
        String urlString = url.toString();
        Link link = new Link(
            0,
            urlString,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            "0",
            0L
        );
        if (linkRepository.findByUrl(urlString).isEmpty()) {
            linkRepository.add(link);
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
                chatLink -> chatLink.getLinkId().equals(linkId)
            ).toList().isEmpty()) {
            linkRepository.delete(url.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<LinkDto> listAll(long tgChatId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLink -> chatLink.getChatId().equals(tgChatId))
            .map(chatLink -> linkRepository.findById(chatLink.getLinkId()).get())
            .map(chatLink -> new LinkDto(
                Long.valueOf(chatLink.getId()),
                chatLink.getUrl(),
                chatLink.getUpdatedAt(),
                chatLink.getLastCheckedAt(),
                chatLink.getLastCommitSha(),
                chatLink.getAnswersCount()
            ))
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
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
            .map(chatLink -> new LinkDto(
                Long.valueOf(chatLink.getId()),
                chatLink.getUrl(),
                chatLink.getUpdatedAt(),
                chatLink.getLastCheckedAt(),
                chatLink.getLastCommitSha(),
                chatLink.getAnswersCount()
            ))
            .toList();
    }

    @Override
    @Transactional
    public void update(LinkDto linkDto) {
        linkRepository.update(
            new Link(
                Math.toIntExact(linkDto.getId()),
                linkDto.getUrl(),
                linkDto.getUpdatedAt(),
                linkDto.getLastCheckedAt(),
                linkDto.getLastCommitSha(),
                linkDto.getAnswersCount()
            )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> listAllByLinkId(Long linkId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getLinkId().equals(linkId))
            .map(ChatLink::getChatId)
            .toList();
    }
}
