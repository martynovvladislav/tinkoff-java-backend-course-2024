package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080)
public class StackOverflowClientTest {
    private WebClientConfiguration webClientConfiguration;

    @BeforeEach
    void initialize() {
        webClientConfiguration = Mockito.mock(WebClientConfiguration.class);
        WebClientConfiguration.SOClientConfig soClientConfig = Mockito.mock(WebClientConfiguration.SOClientConfig.class);
        Mockito.when(webClientConfiguration.soClientConfig()).thenReturn(soClientConfig);
        Mockito.when(soClientConfig.baseUrl()).thenReturn("http://localhost:8080");
    }

    @Test
    void fetchDataTest() {
        String expectedJsonBody = "{\"items\":[{\"title\":\"StackOverflow question example\", " +
            "\"last_activity_date\":1637914338}], \"has_more\":false}";
        stubFor(get(urlEqualTo("/questions/12345?site=stackoverflow"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(expectedJsonBody)
            )
        );
        StackOverflowQuestionsClient stackOverflowQuestionsClient = new StackOverflowQuestionsClient(webClientConfiguration);
        QuestionResponse reposResponse = stackOverflowQuestionsClient.fetchData(
            "12345"
        );

        Assertions.assertEquals(reposResponse.title(), "StackOverflow question example");
        Assertions.assertEquals(reposResponse.lastActivityDate(), OffsetDateTime.parse("2021-11-26T08:12:18Z"));
        verify(getRequestedFor(urlEqualTo("/questions/12345?site=stackoverflow")));
    }
}
