package edu.java.scrapper.clients.stackoverflow;

public interface StackOverflowClient {
    QuestionResponse fetchData(String id);
}
