package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.dtos.stackoverflow.AnswerResponse;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import java.util.List;

public interface StackOverflowClient {
    QuestionResponse fetchData(String id);

    List<AnswerResponse> fetchAnswers(String id);
}
