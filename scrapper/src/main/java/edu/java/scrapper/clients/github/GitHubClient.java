package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.ReposResponseDto;

public interface GitHubClient {
    ReposResponseDto fetchUser(String owner, String repos);
}
