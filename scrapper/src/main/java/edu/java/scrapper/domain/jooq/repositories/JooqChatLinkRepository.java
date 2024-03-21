package edu.java.scrapper.domain.jooq.repositories;

import edu.java.scrapper.domain.jooq.tables.pojos.Chat;
import edu.java.scrapper.domain.jooq.tables.pojos.ChatLink;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.Tables.CHAT_LINK;

@Repository
@RequiredArgsConstructor
public class JooqChatLinkRepository {
    private final DSLContext create;

    public Optional<Chat> find(Long tgChatId, Long linkId) {
        return create.select(CHAT_LINK.fields()).from(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
            .and(CHAT_LINK.LINK_ID.eq(linkId))
            .fetchOptionalInto(Chat.class);
    }

    public void add(Long tgChatId, Long linkId) {
        create.insertInto(
                CHAT_LINK,
                CHAT_LINK.CHAT_ID,
                CHAT_LINK.LINK_ID
            )
            .values(tgChatId, linkId)
            .execute();
    }

    public List<ChatLink> findAll() {
        return create.select(CHAT_LINK.fields()).from(CHAT_LINK).fetchInto(ChatLink.class);
    }

    public void delete(Long tgChatId, Long linkId) {
        create.deleteFrom(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(tgChatId))
            .and(CHAT_LINK.LINK_ID.eq(linkId))
            .execute();
    }
}
