package edu.java.scrapper.services;

import edu.java.scrapper.domain.dtos.LinkDto;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, URI url, OffsetDateTime updatedAt);

    void remove(long tgChatId, URI url);

    List<LinkDto> listAll(long tgChatId);

    List<LinkDto> findOld(long secondThreshold);

    void update(LinkDto linkDto);

    List<Long> listAllByLinkId(Integer linkId);
}
