package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.ReposResponseDto;
import lombok.Builder;
import org.springframework.web.reactive.function.client.WebClient;

@Builder
public class GitHubReposClient implements GitHubClient {
    private final String baseUrl;

    @Override
    public ReposResponseDto fetchUser(String owner, String repos) {
        return WebClient
            .builder()
            .baseUrl(this.baseUrl)
            .build()
            .get()
            .uri("/repos/{owner}/{repos}", owner, repos)
            .retrieve()
            .bodyToMono(ReposResponseDto.class)
            .block();
    }
}
