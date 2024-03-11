package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {

    private WebClientConfiguration webClientConfiguration;

    @BeforeEach
    void initialize() {
        webClientConfiguration = Mockito.mock(WebClientConfiguration.class);
        WebClientConfiguration.GithubClientConfig githubClientConfig = Mockito.mock(WebClientConfiguration.GithubClientConfig.class);
        Mockito.when(webClientConfiguration.githubClientConfig()).thenReturn(githubClientConfig);
        Mockito.when(githubClientConfig.baseUrl()).thenReturn("http://localhost:8080");
    }

    @Test
    void fetchDataTest() {
        String expectedJsonBody = "{\"id\":753126272, " +
            "\"full_name\":\"martynovvladislav/tinkoff-java-backend-course-2024\", " +
            "\"updated_at\":\"2024-02-05T14:20:48Z\", \"cringe\":1111111}";
        stubFor(get(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedJsonBody)
                )
        );
        GitHubReposClient gitHubReposClient = new GitHubReposClient(webClientConfiguration);
        ReposResponseDto reposResponseDto = gitHubReposClient.fetchUser(
            "martynovvladislav", "tinkoff-java-backend-course-2024"
        );

        Assertions.assertEquals(reposResponseDto.id(), 753126272);
        Assertions.assertEquals(reposResponseDto.fullName(), "martynovvladislav/tinkoff-java-backend-course-2024");
        Assertions.assertEquals(reposResponseDto.updatedAt(), OffsetDateTime.parse("2024-02-05T14:20:48Z"));
        verify(getRequestedFor(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024")));
    }
}
