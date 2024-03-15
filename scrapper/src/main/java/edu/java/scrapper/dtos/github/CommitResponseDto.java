package edu.java.scrapper.dtos.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CommitResponseDto(
    @JsonProperty("sha")
    String sha,
    @JsonProperty("commit")
    Commit commit
) {
    public record Commit(@JsonProperty("message") String message) {

    }
}
