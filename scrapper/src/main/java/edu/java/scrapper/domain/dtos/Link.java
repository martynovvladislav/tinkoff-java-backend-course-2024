package edu.java.scrapper.domain.dtos;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Link {
    Integer id;
    String url;
    OffsetDateTime updatedAt;
    OffsetDateTime lastCheckedAt;
}
