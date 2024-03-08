package edu.java.scrapper.clients.github;

import org.springframework.web.reactive.function.client.WebClient;

public class GitHubReposClient implements GitHubClient {
    private final WebClient webClient;
    private final static String DEFAULT_URL = "https://api.github.com";

    public GitHubReposClient() {
        this.webClient = WebClient
            .builder()
            .baseUrl(DEFAULT_URL)
            .build();
    }

    public GitHubReposClient(String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Override
    public ReposResponse fetchUser(String owner, String repos) {
        return this.webClient
            .get()
            .uri("/repos/{owner}/{repos}", owner, repos)
            .retrieve()
            .bodyToMono(ReposResponse.class)
            .block();
    }
}
