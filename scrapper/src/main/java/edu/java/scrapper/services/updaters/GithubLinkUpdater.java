package edu.java.scrapper.services.updaters;

import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.domain.dtos.Link;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GithubLinkUpdater implements LinkUpdater {
    private static final String HOST = "github.com";
    private final GitHubReposClient gitHubReposClient;
    private final LinkService linkService;

    @Override
    public boolean update(Link link) throws URISyntaxException {
        List<String> pathArgs = Arrays.stream(new URI(link.getUrl()).getPath().split("/")).toList();
        ReposResponseDto responseDto = gitHubReposClient.fetchUser(pathArgs.get(1), pathArgs.get(2));
        link.setLastCheckedAt(OffsetDateTime.now());
        if (!link.getUpdatedAt().equals(responseDto.updatedAt())) {
            link.setUpdatedAt(responseDto.updatedAt());
            linkService.update(link);
            return true;
        }
        linkService.update(link);
        return false;
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST);
    }
}
