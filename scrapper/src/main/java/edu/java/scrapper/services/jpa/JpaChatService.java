package edu.java.scrapper.services.jpa;

import edu.java.scrapper.domain.jpa.dtos.Chat;
import edu.java.scrapper.domain.jpa.dtos.Link;
import edu.java.scrapper.domain.jpa.repositories.JpaChatRepository;
import edu.java.scrapper.domain.jpa.repositories.JpaLinkRepository;
import edu.java.scrapper.exceptions.ChatAlreadyExistException;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.services.ChatService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Transactional
    @Override
    public void register(long tgChatId) {
        if (chatRepository.findChatByChatId(tgChatId).isPresent()) {
            throw new ChatAlreadyExistException();
        }

        Chat chat = new Chat();
        chat.setChatId(tgChatId);
        chat.setLinkList(new ArrayList<>());
        chatRepository.save(chat);
    }

    @Transactional
    @Override
    public void unregister(long tgChatId) {
        Optional<Chat> chatOptional = chatRepository.findChatByChatId(tgChatId);
        if (chatOptional.isEmpty()) {
            throw new ChatDoesNotExistException();
        }

        Chat chat = chatOptional.get();
        List<Link> linkListCopy = new ArrayList<>(chat.getLinkList());
        for (Link link : linkListCopy) {
            link.deleteChat(chat);
            if (link.getChatList().isEmpty()) {
                linkRepository.delete(link);
            }
        }
        chatRepository.deleteByChatId(tgChatId);
    }
}
