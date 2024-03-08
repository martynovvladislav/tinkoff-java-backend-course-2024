package edu.java.scrapper.clients.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record QuestionResponse(
     @JsonProperty("title") String title,
     @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
) {}
