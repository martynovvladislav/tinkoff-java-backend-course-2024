package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.util.Optional;

public interface GitHubClient {
    Optional<ReposResponseDto> fetchUser(String owner, String repos);

    Optional<CommitResponseDto> fetchCommit(String owner, String repos);
}
