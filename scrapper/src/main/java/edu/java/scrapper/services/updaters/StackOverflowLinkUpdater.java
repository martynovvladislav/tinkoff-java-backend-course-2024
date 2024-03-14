package edu.java.scrapper.services.updaters;

import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import edu.java.scrapper.domain.dtos.Link;
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
    public boolean update(Link link) throws URISyntaxException {
        String questionId = new URI(link.getUrl()).getPath().split("/")[2];
        QuestionResponse questionResponse = stackOverflowClient.fetchData(questionId);
        link.setLastCheckedAt(OffsetDateTime.now());
        if (!link.getUpdatedAt().equals(questionResponse.lastActivityDate())) {
            link.setUpdatedAt(questionResponse.lastActivityDate());
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
