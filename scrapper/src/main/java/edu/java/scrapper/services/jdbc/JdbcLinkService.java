package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.domain.dtos.ChatLinkDto;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
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

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;
    private final ChatLinkRepository chatLinkRepository;

    @Override
    @Transactional
    public void add(long tgChatId, URI url, OffsetDateTime updatedAt) {
        String urlString = url.toString();
        if (linkRepository.findByUrl(url.toString()).isEmpty()) {
            linkRepository.add(new LinkDto(null, urlString, updatedAt, OffsetDateTime.now()));
        }
        Integer linkId = linkRepository.getLinkId(urlString);
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
        Integer linkId = linkRepository.getLinkId(url.toString());
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
    public List<LinkDto> listAll(long tgChatId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getChatId().equals(tgChatId))
            .map(chatLinkDto -> linkRepository.findById(chatLinkDto.getLinkId()).get())
            .toList();
    }

    @Override
    public List<Long> listAllByLinkId(Integer linkId) {
        return chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getLinkId().equals(linkId))
            .map(ChatLinkDto::getChatId)
            .toList();
    }

    @Override
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
