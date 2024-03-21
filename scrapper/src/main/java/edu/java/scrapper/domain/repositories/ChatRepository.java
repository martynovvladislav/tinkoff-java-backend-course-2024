package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.dtos.ChatDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final JdbcClient jdbcClient;

    public Optional<ChatDto> find(Long tgChatId) {
        String sql = "SELECT chat_id FROM chat WHERE chat_id = ?";
        return jdbcClient.sql(sql)
            .param(tgChatId)
            .query(ChatDto.class)
            .optional();
    }

    public void add(Long tgChatId) {
        String sql = "INSERT INTO chat(chat_id) VALUES (?)";
        jdbcClient.sql(sql)
            .param(tgChatId)
            .update();
    }

    public void delete(Long tgChatId) {
        String sql = "DELETE FROM chat WHERE chat_id = ?";
        jdbcClient.sql(sql)
            .param(tgChatId)
            .update();
    }

    public List<ChatDto> findAll() {
        String sql = "SELECT * FROM chat";
        return jdbcClient.sql(sql)
            .query(ChatDto.class)
            .list();
    }
}
