package edu.java.scrapper.dtos.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class AnswerResponse {
    @JsonProperty("creation_date") OffsetDateTime creationDate;
}
