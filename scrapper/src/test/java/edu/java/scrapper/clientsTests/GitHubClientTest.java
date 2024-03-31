package edu.java.scrapper.clientsTests;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.clients.github.GitHubReposClient;
import edu.java.scrapper.dtos.github.CommitResponseDto;
import edu.java.scrapper.dtos.github.ReposResponseDto;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import edu.java.scrapper.utils.LinearRetryBackoffSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
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
            .retryInstance(Retry.max(1))
            .build();
        ReposResponseDto reposResponseDto = gitHubReposClient.fetchUser(
            "martynovvladislav", "tinkoff-java-backend-course-2024"
        ).get();

        Assertions.assertEquals(reposResponseDto.id(), 753126272);
        Assertions.assertEquals(reposResponseDto.fullName(), "martynovvladislav/tinkoff-java-backend-course-2024");
        Assertions.assertEquals(reposResponseDto.updatedAt(), OffsetDateTime.parse("2024-02-05T14:20:48Z"));
        verify(getRequestedFor(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024")));
    }

    @Test
    void fetchCommitTest() {
        String expectedBody = "[\n" +
            "  {\n" +
            "    \"sha\": \"7e848e08c30bce03dcc210368cfbd0eb9553311f\",\n" +
            "    \"node_id\": \"C_kwDOK9VpKNoAKDdlODQ4ZTA4YzMwYmNlMDNkY2MyMTAzNjhjZmJkMGViOTU1MzMxMWY\",\n" +
            "    \"commit\": {\n" +
            "      \"author\": {\n" +
            "        \"name\": \"Vlad\",\n" +
            "        \"email\": \"martynovq03@gmail.com\",\n" +
            "        \"date\": \"2023-12-24T20:06:57Z\"\n" +
            "      },\n" +
            "      \"committer\": {\n" +
            "        \"name\": \"Vlad\",\n" +
            "        \"email\": \"martynovq03@gmail.com\",\n" +
            "        \"date\": \"2023-12-24T20:06:57Z\"\n" +
            "      },\n" +
            "      \"message\": \"labs\",\n" +
            "      \"tree\": {\n" +
            "        \"sha\": \"12e356db635d8b5c80d3986d6cba099197f114d0\",\n" +
            "        \"url\": \"https://api.github.com/repos/martynovvladislav/JavaLabs/git/trees/12e356db635d8b5c80d3986d6cba099197f114d0\"\n" +
            "      },\n" +
            "      \"url\": \"https://api.github.com/repos/martynovvladislav/JavaLabs/git/commits/7e848e08c30bce03dcc210368cfbd0eb9553311f\",\n" +
            "      \"comment_count\": 0,\n" +
            "      \"verification\": {\n" +
            "        \"verified\": false,\n" +
            "        \"reason\": \"unsigned\",\n" +
            "        \"signature\": null,\n" +
            "        \"payload\": null\n" +
            "      }\n" +
            "    },\n" +
            "    \"url\": \"https://api.github.com/repos/martynovvladislav/JavaLabs/commits/7e848e08c30bce03dcc210368cfbd0eb9553311f\",\n" +
            "    \"html_url\": \"https://github.com/martynovvladislav/JavaLabs/commit/7e848e08c30bce03dcc210368cfbd0eb9553311f\",\n" +
            "    \"comments_url\": \"https://api.github.com/repos/martynovvladislav/JavaLabs/commits/7e848e08c30bce03dcc210368cfbd0eb9553311f/comments\",\n" +
            "    \"author\": {\n" +
            "      \"login\": \"martynovvladislav\",\n" +
            "      \"id\": 53814545,\n" +
            "      \"node_id\": \"MDQ6VXNlcjUzODE0NTQ1\",\n" +
            "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/53814545?v=4\",\n" +
            "      \"gravatar_id\": \"\",\n" +
            "      \"url\": \"https://api.github.com/users/martynovvladislav\",\n" +
            "      \"html_url\": \"https://github.com/martynovvladislav\",\n" +
            "      \"followers_url\": \"https://api.github.com/users/martynovvladislav/followers\",\n" +
            "      \"following_url\": \"https://api.github.com/users/martynovvladislav/following{/other_user}\",\n" +
            "      \"gists_url\": \"https://api.github.com/users/martynovvladislav/gists{/gist_id}\",\n" +
            "      \"starred_url\": \"https://api.github.com/users/martynovvladislav/starred{/owner}{/repo}\",\n" +
            "      \"subscriptions_url\": \"https://api.github.com/users/martynovvladislav/subscriptions\",\n" +
            "      \"organizations_url\": \"https://api.github.com/users/martynovvladislav/orgs\",\n" +
            "      \"repos_url\": \"https://api.github.com/users/martynovvladislav/repos\",\n" +
            "      \"events_url\": \"https://api.github.com/users/martynovvladislav/events{/privacy}\",\n" +
            "      \"received_events_url\": \"https://api.github.com/users/martynovvladislav/received_events\",\n" +
            "      \"type\": \"User\",\n" +
            "      \"site_admin\": false\n" +
            "    },\n" +
            "    \"committer\": {\n" +
            "      \"login\": \"martynovvladislav\",\n" +
            "      \"id\": 53814545,\n" +
            "      \"node_id\": \"MDQ6VXNlcjUzODE0NTQ1\",\n" +
            "      \"avatar_url\": \"https://avatars.githubusercontent.com/u/53814545?v=4\",\n" +
            "      \"gravatar_id\": \"\",\n" +
            "      \"url\": \"https://api.github.com/users/martynovvladislav\",\n" +
            "      \"html_url\": \"https://github.com/martynovvladislav\",\n" +
            "      \"followers_url\": \"https://api.github.com/users/martynovvladislav/followers\",\n" +
            "      \"following_url\": \"https://api.github.com/users/martynovvladislav/following{/other_user}\",\n" +
            "      \"gists_url\": \"https://api.github.com/users/martynovvladislav/gists{/gist_id}\",\n" +
            "      \"starred_url\": \"https://api.github.com/users/martynovvladislav/starred{/owner}{/repo}\",\n" +
            "      \"subscriptions_url\": \"https://api.github.com/users/martynovvladislav/subscriptions\",\n" +
            "      \"organizations_url\": \"https://api.github.com/users/martynovvladislav/orgs\",\n" +
            "      \"repos_url\": \"https://api.github.com/users/martynovvladislav/repos\",\n" +
            "      \"events_url\": \"https://api.github.com/users/martynovvladislav/events{/privacy}\",\n" +
            "      \"received_events_url\": \"https://api.github.com/users/martynovvladislav/received_events\",\n" +
            "      \"type\": \"User\",\n" +
            "      \"site_admin\": false\n" +
            "    },\n" +
            "    \"parents\": [\n" +
            "\n" +
            "    ]\n" +
            "  }\n" +
            "]";

        stubFor(get(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024/commits"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(expectedBody)
            )
        );

        GitHubReposClient gitHubReposClient = GitHubReposClient.builder()
            .webClient(WebClient.builder().baseUrl("http://localhost:8080").build())
            .retryInstance(Retry.max(1))
            .build();
        CommitResponseDto commitResponseDto = gitHubReposClient.fetchCommit("martynovvladislav", "tinkoff-java-backend-course-2024").get();

        Assertions.assertEquals(commitResponseDto.sha(), "7e848e08c30bce03dcc210368cfbd0eb9553311f");
        Assertions.assertEquals(commitResponseDto.commit().message(), "labs");
    }

    @Test
    void linearRetryWithFilterTest() {
        stubFor(get(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024/commits"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("")
                    .withStatus(404)
            )
        );

        GitHubReposClient gitHubReposClient = GitHubReposClient.builder()
            .webClient(WebClient.builder().baseUrl("http://localhost:8080").build())
            .retryInstance(LinearRetryBackoffSpec.linearBackoff(3, Duration.ofMillis(1)).filter(
                e -> ((WebClientResponseException) e).getStatusCode().value() == 404
            ))
            .build();

        Assertions.assertEquals(
            gitHubReposClient.fetchCommit("martynovvladislav", "tinkoff-java-backend-course-2024"),
            Optional.empty()
        );
    }
}
