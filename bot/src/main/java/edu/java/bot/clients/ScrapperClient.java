package edu.java.bot.clients;

import edu.java.bot.dtos.AddLinkRequestDto;
import edu.java.bot.dtos.LinkResponseDto;
import edu.java.bot.dtos.ListLinkResponseDto;
import edu.java.bot.dtos.RemoveLinkRequestDto;

public interface ScrapperClient {
    ListLinkResponseDto getLinks(Long tgChatId);

    LinkResponseDto addLink(Long tgChatId, AddLinkRequestDto addLinkRequestDto);

    LinkResponseDto deleteLink(Long tgChatId, RemoveLinkRequestDto removeLinkRequestDto);

    void registerChat(Long id);

    void deleteChat(Long id);
}
