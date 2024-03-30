package edu.java.scrapper.domain.dtos;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LinkDto {
    Long id;
    String url;
    OffsetDateTime updatedAt;
    OffsetDateTime lastCheckedAt;
    String lastCommitSha;
    Long answersCount;
}
