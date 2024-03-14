package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.domain.dtos.ChatLink;
import edu.java.scrapper.domain.dtos.Link;
import edu.java.scrapper.domain.repositories.ChatLinkRepository;
import edu.java.scrapper.domain.repositories.ChatRepository;
import edu.java.scrapper.domain.repositories.LinkRepository;
import edu.java.scrapper.exceptions.ChatAlreadyExistException;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.services.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatLinkRepository chatLinkRepository;
    private final LinkRepository linkRepository;

    @Override
    @Transactional
    public void register(long tgChatId) {
        if (chatRepository.find(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException();
        }

        chatRepository.add(tgChatId);
    }

    @Override
    @Transactional
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
                            chatLink -> chatLink.getLinkId().equals(link.getId())
                        ).toList().isEmpty()
                ) {
                    linkRepository.delete(link.getUrl());
                }
            }
        );
        chatRepository.delete(tgChatId);
    }
}
