package edu.java.github;

public interface GitHubClient {
    ReposResponse fetchUser(String owner, String repos);
}
