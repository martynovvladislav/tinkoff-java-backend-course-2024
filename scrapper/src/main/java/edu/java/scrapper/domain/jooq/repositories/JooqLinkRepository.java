package edu.java.scrapper.domain.jooq.repositories;

import edu.java.scrapper.domain.jooq.tables.pojos.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.scrapper.domain.jooq.Tables.LINK;
import static org.jooq.impl.DSL.row;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository {
    private final DSLContext create;

    public Optional<Link> findByUrl(String url) {
        return create.select(LINK.fields()).from(LINK).where(LINK.URL.eq(url)).fetchOptionalInto(Link.class);
    }

    public Optional<Link> findById(Long id) {
        return create.select(LINK.fields()).from(LINK).where(LINK.ID.eq(Math.toIntExact(id)))
            .fetchOptionalInto(Link.class);
    }

    public Long getLinkId(String url) {
        return Long.valueOf(create.select().from(LINK).where(LINK.URL.eq(url)).fetch(LINK.ID).getFirst());
    }

    public void add(Link link) {
        create.insertInto(
                LINK,
                LINK.URL,
                LINK.UPDATED_AT,
                LINK.LAST_CHECKED_AT,
                LINK.LAST_COMMIT_SHA,
                LINK.ANSWERS_COUNT
            ).values(
                link.getUrl(),
                link.getUpdatedAt(),
                link.getLastCheckedAt(),
                link.getLastCommitSha(),
                link.getAnswersCount()
            )
            .execute();
    }

    public void delete(String url) {
        create.deleteFrom(LINK).where(LINK.URL.eq(url)).execute();
    }

    public List<Link> findAll() {
        return create.select(LINK.fields()).from(LINK).fetchInto(Link.class);
    }

    public void update(Link link) {
        create.update(LINK).set(
                row(LINK.URL, LINK.UPDATED_AT, LINK.LAST_CHECKED_AT, LINK.LAST_COMMIT_SHA, LINK.ANSWERS_COUNT),
                row(
                    link.getUrl(),
                    link.getUpdatedAt(),
                    link.getLastCheckedAt(),
                    link.getLastCommitSha(),
                    link.getAnswersCount()
                )
            ).where(LINK.URL.eq(link.getUrl()))
            .execute();
    }
}
