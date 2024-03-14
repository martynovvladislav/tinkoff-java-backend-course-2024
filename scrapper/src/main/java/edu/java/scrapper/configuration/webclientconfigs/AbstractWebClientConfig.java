package edu.java.scrapper.configuration.webclientconfigs;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class AbstractWebClientConfig {
    String baseUrl;
}
