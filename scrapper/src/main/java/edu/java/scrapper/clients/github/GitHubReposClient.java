package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;

@Builder
public class GitHubReposClient implements GitHubClient {
    private final WebClient webClient;

    @Override
    public ReposResponseDto fetchUser(String owner, String repos) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repos}", owner, repos)
            .retrieve()
            .bodyToMono(ReposResponseDto.class)
            .block();
    }

    @Override
    public Optional<CommitResponseDto> fetchCommit(String owner, String repos) {
        return Objects.requireNonNull(Objects.requireNonNull(this.webClient
            .get()
            .uri("/repos/{owner}/{repos}/commits", owner, repos)
            .retrieve()
            .toEntityList(CommitResponseDto.class)
            .block()).getBody()).stream().findFirst();
    }
}
