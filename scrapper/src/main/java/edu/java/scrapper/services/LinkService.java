package edu.java.scrapper.services;

import edu.java.scrapper.domain.dtos.LinkDto;
import java.net.URI;
import java.util.List;

public interface LinkService {
    void add(Long tgChatId, URI url);

    void remove(long tgChatId, URI url);

    List<LinkDto> listAll(long tgChatId);

    List<LinkDto> findOld(long secondThreshold);

    void update(LinkDto linkDto);

    List<Long> listAllByLinkId(Long linkId);


}
