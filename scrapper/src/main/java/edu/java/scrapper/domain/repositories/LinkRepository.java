package edu.java.scrapper.domain.repositories;

import edu.java.scrapper.domain.dtos.Link;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepository {
    private final JdbcClient jdbcClient;

    public Optional<Link> findByUrl(String url) {
        String sql = "SELECT id,url,updated_at,last_checked_at FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .params(url)
            .query(Link.class)
            .optional();
    }

    public Optional<Link> findById(Integer id) {
        String sql = "SELECT id,url,updated_at,last_checked_at FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .params(id)
            .query(Link.class)
            .optional();
    }

    public String getLinkUrl(Integer linkId) {
        String sql = "SELECT url FROM link WHERE id = ?";
        return jdbcClient.sql(sql)
            .params(linkId)
            .query(String.class)
            .single();
    }

    public Integer getLinkId(String url) {
        String sql = "SELECT id FROM link WHERE url = ?";
        return jdbcClient.sql(sql)
            .param(url)
            .query(Integer.class)
            .single();
    }

    public Integer add(Link link) {
        if (findByUrl(link.getUrl()).isEmpty()) {
            String sql = "INSERT INTO link(url,updated_at,last_checked_at) VALUES(?, ?, ?) RETURNING id";
            return jdbcClient.sql(sql)
                .params(link.getUrl(), link.getUpdatedAt(), link.getLastCheckedAt())
                .query(Integer.class)
                .single();
        }
        return getLinkId(link.getUrl());
    }

    public void delete(String url) {
        if (findByUrl(url).isPresent()) {
            String sql = "DELETE FROM link WHERE url = ?";
            jdbcClient.sql(sql)
                .params(url)
                .update();
        }

    }

    public List<Link> findAll() {
        String sql = "SELECT * FROM link";
        return jdbcClient.sql(sql)
            .query(Link.class)
            .list();
    }

    public void update(Link link) {
        String sql = "UPDATE link SET updated_at = ?, last_checked_at = ? WHERE id = ?";
        jdbcClient.sql(sql)
            .params(link.getUpdatedAt(), link.getLastCheckedAt(), link.getId())
            .update();
    }
}
