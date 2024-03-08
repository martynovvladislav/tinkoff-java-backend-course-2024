package edu.java.scrapper.clients.github;

import edu.java.scrapper.dtos.github.ReposResponse;

public interface GitHubClient {
    ReposResponse fetchUser(String owner, String repos);
}
