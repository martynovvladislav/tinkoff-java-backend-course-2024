package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.dtos.LinkDto;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepository {
    private final JdbcClient jdbcClient;

    public Optional<LinkDto> findByUrl(String url) {
        String sql = "SELECT id,url,updated_at,last_checked_at FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .params(url)
            .query(LinkDto.class)
            .optional();
    }

    public Optional<LinkDto> findById(Integer id) {
        String sql = "SELECT id,url,updated_at,last_checked_at FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .params(id)
            .query(LinkDto.class)
            .optional();
    }

    public Integer getLinkId(String url) {
        String sql = "SELECT id FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(Integer.class)
            .single();
    }

    public Integer add(LinkDto linkDto) {
        if (findByUrl(linkDto.getUrl()).isEmpty()) {
            String sql = "INSERT INTO link(url,updated_at,last_checked_at) VALUES(?, ?, ?) RETURNING id";
            return jdbcClient.sql(sql)
                .params(linkDto.getUrl(), linkDto.getUpdatedAt(), linkDto.getLastCheckedAt())
                .query(Integer.class)
                .single();
        }
        return getLinkId(linkDto.getUrl());
    }

    public void delete(String url) {
        if (findByUrl(url).isPresent()) {
            String sql = "DELETE FROM link WHERE url = ?";
            jdbcClient.sql(sql)
                .params(url)
                .update();
        }

    }

    public List<LinkDto> findAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(LinkDto.class)
            .list();
    }

    public List<LinkDto> findOld(long secondThreshold) {
        OffsetDateTime thresholdTime = OffsetDateTime.now().minusSeconds(secondThreshold);
        String sql = "SELECT * FROM link WHERE last_checked_at <= ?";
        return jdbcClient.sql(sql)
            .params(thresholdTime)
            .query(LinkDto.class)
            .list();
    }

    public void update(LinkDto linkDto) {
        String sql = "UPDATE link SET updated_at = ?, last_checked_at = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(linkDto.getUpdatedAt(), linkDto.getLastCheckedAt(), linkDto.getId())
            .update();
    }
}
