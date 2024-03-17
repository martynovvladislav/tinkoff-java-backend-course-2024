package edu.java.scrapper.services.jooq;

import edu.java.scrapper.domain.jooq.repositories.JooqChatLinkRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqChatRepository;
import edu.java.scrapper.domain.jooq.repositories.JooqLinkRepository;
import edu.java.scrapper.domain.jooq.tables.pojos.ChatLink;
import edu.java.scrapper.domain.jooq.tables.pojos.Link;
import edu.java.scrapper.exceptions.ChatAlreadyExistException;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.services.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JooqChatService implements ChatService {
    private final JooqChatRepository chatRepository;
    private final JooqChatLinkRepository chatLinkRepository;
    private final JooqLinkRepository linkRepository;

    @Override
    public void register(long tgChatId) {
        if (chatRepository.find(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException();
        }

        chatRepository.add(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        if (chatRepository.find(tgChatId).isEmpty()) {
            throw new ChatDoesNotExistException();
        }
        List<ChatLink> chatLinks = chatLinkRepository.findAll().stream()
            .filter(chatLink -> chatLink.getChatId().equals(tgChatId))
            .toList();
        chatLinks.forEach(
            chatLink -> chatLinkRepository.delete(tgChatId, chatLink.getLinkId())
        );

        chatLinks = chatLinkRepository.findAll();
        List<Link> links = linkRepository.findAll();

        List<ChatLink> finalChatLinks = chatLinks;
        links.forEach(link -> {
                if (
                    finalChatLinks.stream()
                        .filter(
                            chatLink -> chatLink.getLinkId().equals(Long.valueOf(link.getId()))
                        ).toList().isEmpty()
                ) {
                    linkRepository.delete(link.getUrl());
                }
            }
        );
        chatRepository.delete(tgChatId);
    }
}
