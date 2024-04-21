package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.dtos.stackoverflow.AnswerResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import java.util.List;
import java.util.Optional;

public interface StackOverflowClient {
    Optional<QuestionResponse> fetchData(String id);

    List<AnswerResponse> fetchAnswers(String id);
}
