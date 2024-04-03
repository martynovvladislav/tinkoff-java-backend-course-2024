package edu.java.scrapper.domain.jooq.repositories;

import edu.java.scrapper.domain.jooq.tables.pojos.Chat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.Tables.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository {
    private final DSLContext create;

    public Optional<Chat> find(Long tgChatId) {
        return create.select().from(CHAT).where(CHAT.CHAT_ID.eq(tgChatId)).fetchOptionalInto(Chat.class);
    }

    public void add(Long tgChatId) {
        create.insertInto(CHAT, CHAT.CHAT_ID).values(tgChatId).execute();
    }

    public void delete(Long tgChatId) {
        create.deleteFrom(CHAT).where(CHAT.CHAT_ID.eq(tgChatId)).execute();
    }

    public List<Chat> findAll() {
        return create.select().from(CHAT).fetchInto(Chat.class);
    }
}
