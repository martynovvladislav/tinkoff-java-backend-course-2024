package edu.java.scrapper.clients.github;

public interface GitHubClient {
    ReposResponse fetchUser(String owner, String repos);
}
