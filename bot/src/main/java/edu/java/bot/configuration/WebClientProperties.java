package edu.java.bot.configuration;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WebClientProperties {
    String baseUrl;
    Integer retryAttempts;
    Long retryDuration;
    String retryPolicy;
    List<Integer> retryCodes;
}
