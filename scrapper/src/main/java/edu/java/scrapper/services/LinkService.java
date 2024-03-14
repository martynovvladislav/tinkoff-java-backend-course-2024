package edu.java.scrapper.services;

import edu.java.scrapper.domain.dtos.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    void add(long tgChatId, URI url, OffsetDateTime updatedAt);

    void remove(long tgChatId, URI url);

    List<Link> listAll(long tgChatId);

    List<Link> findOld(long secondThreshold);

    void update(Link link);

    List<Long> listAllByLinkId(Integer linkId);
}
