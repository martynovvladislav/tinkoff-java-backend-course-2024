package edu.java.scrapper.services.jdbc;

import edu.java.scrapper.domain.jdbc.dtos.ChatLinkDto;
import edu.java.scrapper.domain.jdbc.dtos.LinkDto;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcChatRepository;
import edu.java.scrapper.domain.jdbc.repositories.JdbcLinkRepository;
import edu.java.scrapper.exceptions.ChatAlreadyExistException;
import edu.java.scrapper.exceptions.ChatDoesNotExistException;
import edu.java.scrapper.services.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;
    private final JdbcChatLinkRepository chatLinkRepository;
    private final JdbcLinkRepository linkRepository;

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
        List<ChatLinkDto> chatLinkDtos = chatLinkRepository.findAll().stream()
            .filter(chatLinkDto -> chatLinkDto.getChatId().equals(tgChatId))
            .toList();
        chatLinkDtos.forEach(
                chatLinkDto -> chatLinkRepository.delete(tgChatId, chatLinkDto.getLinkId())
        );

        chatLinkDtos = chatLinkRepository.findAll();
        List<LinkDto> linkDtos = linkRepository.findAll();

        List<ChatLinkDto> finalChatLinkDtos = chatLinkDtos;
        linkDtos.forEach(link -> {
                if (
                    finalChatLinkDtos.stream()
                        .filter(
                                chatLinkDto -> chatLinkDto.getLinkId().equals(link.getId())
                        ).toList().isEmpty()
                ) {
                    linkRepository.delete(link.getUrl());
                }
            }
        );
        chatRepository.delete(tgChatId);
    }
}
