package edu.java.scrapper.domain.jpa.repositories;

import edu.java.scrapper.domain.jpa.dtos.Link;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUrl(String url);

    @NotNull Optional<Link> findById(@NotNull Long id);

    @NotNull List<Link> findAll();
}
