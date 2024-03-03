package edu.java.scrapper.dtos.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionsResponse(
    @JsonProperty("items") List<QuestionResponse> questionResponseList
) {}
