package edu.java.scrapper.services.updaters;

import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final StackOverflowQuestionsClient stackOverflowClient;
    private final LinkService linkService;
    private static final String HOST = "stackoverflow.com";

    @Override
    public boolean update(LinkDto linkDto) throws URISyntaxException {
        String questionId = new URI(linkDto.getUrl()).getPath().split("/")[2];
        QuestionResponse questionResponse = stackOverflowClient.fetchData(questionId);
        linkDto.setLastCheckedAt(OffsetDateTime.now());
        if (!linkDto.getUpdatedAt().equals(questionResponse.lastActivityDate())) {
            linkDto.setUpdatedAt(questionResponse.lastActivityDate());
            linkService.update(linkDto);
            return true;
        }
        linkService.update(linkDto);
        return false;
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST);
    }
}
