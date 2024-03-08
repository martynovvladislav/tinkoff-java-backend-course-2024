package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;

public interface StackOverflowClient {
    QuestionResponse fetchData(String id);
}
