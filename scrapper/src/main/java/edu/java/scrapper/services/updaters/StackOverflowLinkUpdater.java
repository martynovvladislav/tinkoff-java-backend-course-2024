package edu.java.scrapper.services.updaters;

import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import edu.java.scrapper.domain.dtos.LinkDto;
import edu.java.scrapper.dtos.stackoverflow.AnswerResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.services.LinkService;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StackOverflowLinkUpdater implements LinkUpdater {
    private final StackOverflowQuestionsClient stackOverflowClient;
    private final LinkService linkService;
    private static final String HOST = "stackoverflow.com";

    @Override
    public String update(LinkDto linkDto) throws URISyntaxException {
        String questionId = new URI(linkDto.getUrl()).getPath().split("/")[2];
        QuestionResponse questionResponse = stackOverflowClient.fetchData(questionId);
        List<AnswerResponse> answerResponses = stackOverflowClient.fetchAnswers(questionId);
        linkDto.setLastCheckedAt(OffsetDateTime.now());

        if (!answerResponses.isEmpty() && answerResponses.size() != linkDto.getAnswersCount()) {
            if (answerResponses.size() != linkDto.getAnswersCount()) {
                linkDto.setUpdatedAt(questionResponse.lastActivityDate());
                linkDto.setAnswersCount((long) answerResponses.size());
                linkService.update(linkDto);
                return questionResponse.title() + "\n--\nWAS UPDATED:\n--\nnew answers (added on "
                    + answerResponses.get(0).getCreationDate()
                    .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)) + ")\n--";
            }
        }
        if (!linkDto.getUpdatedAt().toZonedDateTime().withNano(0)
            .equals(questionResponse.lastActivityDate().toZonedDateTime()
                .withZoneSameInstant(linkDto.getUpdatedAt().toZonedDateTime().getZone()).withNano(0))
        ) {
            linkDto.setUpdatedAt(questionResponse.lastActivityDate());
            linkService.update(linkDto);
            return questionResponse.title() + "\n--\nWAS UPDATED\n--";
        }
        linkService.update(linkDto);
        return null;
    }

    @Override
    public boolean supports(URI url) {
        return url.getHost().equals(HOST);
    }
}
