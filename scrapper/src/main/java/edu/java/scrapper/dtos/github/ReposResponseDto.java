package edu.java.scrapper.dtos.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record ReposResponseDto(
    @JsonProperty("id") Long id,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("updated_at") OffsetDateTime updatedAt
    ) {}
