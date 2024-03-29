package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.util.Objects;
import java.util.Optional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Builder
@Slf4j
public class GitHubReposClient implements GitHubClient {
    private final WebClient webClient;
    private final Retry retryInstance;

    @Override
    public Optional<ReposResponseDto> fetchUser(String owner, String repos) {
        try {
            return Optional.ofNullable(this.webClient
                .get()
                .uri("/repos/{owner}/{repos}", owner, repos)
                .retrieve()
                .bodyToMono(ReposResponseDto.class)
                .retryWhen(retryInstance)
                .block());
        } catch (Exception e) {
            log.info("Exception in GithubClient#fetchUser: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CommitResponseDto> fetchCommit(String owner, String repos) {
        try {
            return Objects.requireNonNull(Objects.requireNonNull(this.webClient
                .get()
                .uri("/repos/{owner}/{repos}/commits", owner, repos)
                .retrieve()
                .toEntityList(CommitResponseDto.class)
                .retryWhen(retryInstance)
                .block()).getBody()).stream().findFirst();
        } catch (Exception e) {
            log.info("Exception in GithubClient#fetchCommit: " + e.getMessage());
            return Optional.empty();
        }
    }
}
