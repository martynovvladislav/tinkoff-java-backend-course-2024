package edu.java.scrapper.domain.jpa.repositories;

import edu.java.scrapper.domain.jpa.dtos.Chat;
import edu.java.scrapper.domain.jpa.dtos.Link;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findChatByChatId(Long tgChatId);

    void deleteByChatId(Long tgChatId);

    List<Chat> findAllByLinkListContaining(Link link);
}
