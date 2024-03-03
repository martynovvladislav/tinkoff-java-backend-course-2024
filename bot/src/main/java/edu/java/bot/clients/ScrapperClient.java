package edu.java.bot.clients;

import edu.java.bot.dtos.AddLinkRequest;
import edu.java.bot.dtos.LinkResponse;
import edu.java.bot.dtos.ListLinkResponse;
import edu.java.bot.dtos.RemoveLinkRequest;

public interface ScrapperClient {
    ListLinkResponse getLinks(Long tgChatId);

    LinkResponse addLink(Long tgChatId, AddLinkRequest addLinkRequest);

    LinkResponse deleteLink(Long tgChatId, RemoveLinkRequest removeLinkRequest);

    void registerChat(Long id);

    void deleteChat(Long id);
}
