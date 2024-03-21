package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080)
public class GitHubClientTest {
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
        GitHubReposClient gitHubReposClient = GitHubReposClient.builder()
            .webClient(WebClient.builder().baseUrl("http://localhost:8080").build())
            .build();
        ReposResponseDto reposResponseDto = gitHubReposClient.fetchUser(
            "martynovvladislav", "tinkoff-java-backend-course-2024"
        );

        Assertions.assertEquals(reposResponseDto.id(), 753126272);
        Assertions.assertEquals(reposResponseDto.fullName(), "martynovvladislav/tinkoff-java-backend-course-2024");
        Assertions.assertEquals(reposResponseDto.updatedAt(), OffsetDateTime.parse("2024-02-05T14:20:48Z"));
        verify(getRequestedFor(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024")));
    }

    @Test
    void fetchCommitTest() {
        String expectedBody = "[\n" +
            "  {\n" +
            "    \"sha\": \"e310dfb3d21f1a3f07b5fa8267e9f7c2e3f30b15\",\n" +
            "    \"commit\": {\n" +
            "      \"author\": {\n" +
            "        \"name\": \"Vladislav\",\n" +
            "        \"email\": \"53814545+martynovvladislav@users.noreply.github.com\",\n" +
            "        \"date\": \"2024-03-08T13:22:42Z\"\n" +
            "      },\n" +
            "      \"committer\": {\n" +
            "        \"name\": \"GitHub\",\n" +
            "        \"email\": \"noreply@github.com\",\n" +
            "        \"date\": \"2024-03-08T13:22:42Z\"\n" +
            "      },\n" +
            "      \"message\": \"Merge pull request #2 from martynovvladislav/hw2\\n\\nhw2 PR\",\n" +
            "      \"tree\": {\n" +
            "        \"sha\": \"bb4e8582f57ca0f8e79b682d7318a1680da6e1e1\",\n" +
            "        \"url\": \"https://api.github.com/repos/martynovvladislav/tinkoff-java-backend-course-2024/git/trees/bb4e8582f57ca0f8e79b682d7318a1680da6e1e1\"\n" +
            "      }\n" +
            "  }]";

        stubFor(get(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024/commits"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(expectedBody)
            )
        );

        GitHubReposClient gitHubReposClient = GitHubReposClient.builder()
            .webClient(WebClient.builder().baseUrl("https://api.github.com").build())
            .build();
        CommitResponseDto commitResponseDto = gitHubReposClient.fetchCommit("martynovvladislav", "tinkoff-java-backend-course-2024").get();

        Assertions.assertEquals(commitResponseDto.sha(), "e310dfb3d21f1a3f07b5fa8267e9f7c2e3f30b15");
        Assertions.assertEquals(commitResponseDto.commit().message(), "Merge pull request #2 from martynovvladislav/hw2\n\nhw2 PR");
    }
}
