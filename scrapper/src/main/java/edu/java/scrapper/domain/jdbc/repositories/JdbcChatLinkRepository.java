package edu.java.scrapper.domain.jdbc.repositories;

import edu.java.scrapper.domain.dtos.ChatLinkDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcChatLinkRepository {
    private final JdbcClient jdbcClient;

    public Optional<ChatLinkDto> find(Long tgChatId, Long linkId) {
        String sql = "SELECT * FROM chat_link WHERE chat_id = ? AND link_id = ?";
        return jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .query(ChatLinkDto.class)
            .optional();
    }

    public void add(Long tgChatId, Long linkId) {
        String sql = "INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)";
        jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .update();
    }

    public List<ChatLinkDto> findAll() {
        String sql = "SELECT * FROM chat_link";
        return jdbcClient.sql(sql)
            .query(ChatLinkDto.class)
            .list();
    }

    public List<ChatLinkDto> findAllByLinkId(Long linkId) {
        String sql = "SELECT * FROM chat_link WHERE link_id = ?";
        return jdbcClient.sql(sql)
            .params(linkId)
            .query(ChatLinkDto.class)
            .list();
    }

    public void delete(Long tgChatId, Long linkId) {
        String sql = "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?";
        jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .update();
    }
}
