package edu.java.scrapper.clients.github;

import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubReposClient implements GitHubClient {
    private final WebClient webClient;

    private final WebClientConfiguration webClientConfiguration;

    public GitHubReposClient(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
        this.webClient = WebClient
            .builder()
            .baseUrl(this.webClientConfiguration.githubClientConfig().baseUrl())
            .build();
    }

    @Override
    public ReposResponseDto fetchUser(String owner, String repos) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repos}", owner, repos)
            .retrieve()
            .bodyToMono(ReposResponseDto.class)
            .block();
    }
}
