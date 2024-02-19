package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.github.GitHubReposClient;
import edu.java.github.ReposResponse;
import edu.java.stackoverflow.QuestionResponse;
import edu.java.stackoverflow.StackOverflowQuestionsClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

public class StackOverflowClientTest {
    private static WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stop() {
        wireMockServer.stop();
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
        StackOverflowQuestionsClient stackOverflowQuestionsClient = new StackOverflowQuestionsClient("http://localhost:" + wireMockServer.port());
        QuestionResponse reposResponse = stackOverflowQuestionsClient.fetchData(
            "12345"
        );

        Assertions.assertEquals(reposResponse.title(), "StackOverflow question example");
        Assertions.assertEquals(reposResponse.lastActivityDate(), OffsetDateTime.parse("2021-11-26T08:12:18Z"));
        verify(getRequestedFor(urlEqualTo("/questions/12345?site=stackoverflow")));
    }
}
