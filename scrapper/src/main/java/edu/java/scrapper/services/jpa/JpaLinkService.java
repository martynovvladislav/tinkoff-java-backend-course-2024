package edu.java.scrapper.services.jpa;

import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import edu.java.scrapper.domain.jpa.dtos.Chat;
import edu.java.scrapper.domain.jpa.dtos.Link;
import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.exceptions.LinkAlreadyExistException;
import edu.java.scrapper.exceptions.LinkDoesNotExistException;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Transactional
    @Override
    public void add(Long tgChatId, URI url) {
        String urlString = url.toString();
        Optional<Chat> chatOptional = chatRepository.findChatByChatId(tgChatId);
        if (chatOptional.isEmpty()) {
            throw new ChatDoesNotExistException();
        }

        Chat chat = chatOptional.get();
        Optional<Link> linkOptional = linkRepository.findByUrl(urlString);
        Link link;
        if (linkOptional.isEmpty()) {
            link = new Link();
            link.setUrl(urlString);
            link.setUpdatedAt(OffsetDateTime.now());
            link.setLastCheckedAt(OffsetDateTime.now());
            link.setLastCommitSha("0");
            link.setAnswersCount(0L);
            link.setChatList(new ArrayList<>());
        } else {
            link = linkOptional.get();
            if (chat.linkList.contains(link)) {
                throw new LinkAlreadyExistException();
            }
        }
        link.addChat(chat);
        linkRepository.save(link);
    }

    @Transactional
    @Override
    public void remove(long tgChatId, URI url) {
        String urlString = url.toString();
        Optional<Chat> chatOptional = chatRepository.findChatByChatId(tgChatId);
        if (chatOptional.isEmpty()) {
            throw new ChatDoesNotExistException();
        }

        Chat chat = chatOptional.get();
        Optional<Link> linkOptional = linkRepository.findByUrl(urlString);
        if (linkOptional.isEmpty()) {
            throw new LinkDoesNotExistException();
        } else {
            Link link = linkOptional.get();
            if (!chat.linkList.contains(link)) {
                throw new LinkDoesNotExistException();
            }

            link.deleteChat(chat);
            if (link.chatList.isEmpty()) {
                linkRepository.delete(link);
            } else {
                linkRepository.save(link);
            }
        }
    }

    @Transactional
    @Override
    public List<LinkDto> listAll(long tgChatId) {
        return linkRepository.findAll().stream()
            .map(link -> new LinkDto(
                link.getId(),
                link.getUrl(),
                link.getUpdatedAt(),
                link.getLastCheckedAt(),
                link.getLastCommitSha(),
                link.getAnswersCount()
            ))
            .toList();
    }

    @Transactional
    @Override
    public List<LinkDto> findOld(long secondThreshold) {
        return linkRepository.findAll().stream()
            .map(link -> new LinkDto(
                link.getId(),
                link.getUrl(),
                link.getUpdatedAt(),
                link.getLastCheckedAt(),
                link.getLastCommitSha(),
                link.getAnswersCount()
            ))
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

    @Transactional
    @Override
    public void update(LinkDto linkDto) {
        Link link = linkRepository.findByUrl(linkDto.getUrl()).get();
        link.setUpdatedAt(linkDto.getUpdatedAt());
        link.setLastCheckedAt(linkDto.getLastCheckedAt());
        link.setLastCommitSha(linkDto.getLastCommitSha());
        link.setAnswersCount(linkDto.getAnswersCount());
        linkRepository.save(link);
    }

    @Transactional
    @Override
    public List<Long> listAllByLinkId(Long linkId) {
        Link link = linkRepository.findById(linkId).get();
        return chatRepository.findAllByLinkListContaining(link).stream()
            .map(Chat::getChatId)
            .toList();
    }
}
