package edu.java.scrapper.services.updaters;

import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GithubLinkUpdater implements LinkUpdater {
    private static final String HOST = "github.com";
    private static final int OWNER_INDEX = 2;
    private static final int REPOS_INDEX = 3;
    private final GitHubReposClient gitHubReposClient;
    private final LinkService linkService;

    @Override
    public String update(LinkDto linkDto) throws URISyntaxException {
        List<String> pathArgs = Arrays.stream(new URI(linkDto.getUrl()).getPath().split("/")).toList();
        ReposResponseDto responseDto =
            gitHubReposClient.fetchUser(pathArgs.get(OWNER_INDEX), pathArgs.get(REPOS_INDEX));
        Optional<CommitResponseDto> commitResponseDto =
            gitHubReposClient.fetchCommit(pathArgs.get(OWNER_INDEX), pathArgs.get(REPOS_INDEX));
        linkDto.setLastCheckedAt(OffsetDateTime.now());
        if (commitResponseDto.isPresent()) {
            if (linkDto.getLastCommitSha() == null
                || !commitResponseDto.get().sha().equals(linkDto.getLastCommitSha())) {
                linkDto.setUpdatedAt(responseDto.updatedAt());
                linkDto.setLastCommitSha(commitResponseDto.get().sha());
                linkService.update(linkDto);
                return responseDto.fullName() + "WAS UPDATED:\n" + commitResponseDto.get().commit().message();
            }
        }

        if (!linkDto.getUpdatedAt().equals(responseDto.updatedAt())) {
            linkDto.setUpdatedAt(responseDto.updatedAt());
            linkService.update(linkDto);
            return responseDto.fullName() + "WAS UPDATED";
        }
        linkService.update(linkDto);
        return null;
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST);
    }
}
