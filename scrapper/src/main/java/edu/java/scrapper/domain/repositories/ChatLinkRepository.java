package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.dtos.ChatLinkDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatLinkRepository {
    private final JdbcClient jdbcClient;

    public Optional<ChatLinkDto> find(Long tgChatId, Integer linkId) {
        String sql = "SELECT * FROM connections WHERE chat_id = ? AND link_id = ?";
        return jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .query(ChatLinkDto.class)
            .optional();
    }

    public void add(Long tgChatId, Integer linkId) {
        String sql = "INSERT INTO connections (chat_id, link_id) VALUES (?, ?)";
        jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .update();
    }

    public List<ChatLinkDto> findAll() {
        String sql = "SELECT * FROM connections";
        return jdbcClient.sql(sql)
            .query(ChatLinkDto.class)
            .list();
    }

    public void delete(Long tgChatId, Integer linkId) {
        String sql = "DELETE FROM connections WHERE chat_id = ? AND link_id = ?";
        jdbcClient.sql(sql)
            .params(tgChatId, linkId)
            .update();
    }
}
