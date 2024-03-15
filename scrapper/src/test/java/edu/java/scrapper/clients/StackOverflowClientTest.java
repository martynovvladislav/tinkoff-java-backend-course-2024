package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.configuration.WebClientConfiguration;
import edu.java.scrapper.dtos.stackoverflow.QuestionResponse;
import edu.java.scrapper.clients.stackoverflow.StackOverflowQuestionsClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.OffsetDateTime;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

@WireMockTest(httpPort = 8080) public class StackOverflowClientTest {
    @Test void fetchDataTest() {
        String expectedJsonBody = "{\"items\":[{\"title\":\"StackOverflow question example\", " +
            "\"last_activity_date\":1637914338}], \"has_more\":false}";
        stubFor(get(urlEqualTo("/questions/12345?site=stackoverflow")).willReturn(aResponse().withHeader(
            "Content-Type",
            "application/json"
        ).withBody(expectedJsonBody)));
        StackOverflowQuestionsClient stackOverflowQuestionsClient = StackOverflowQuestionsClient.builder()
            .webClient(WebClient.builder().baseUrl("http://localhost:8080").build()).build();
        QuestionResponse reposResponse = stackOverflowQuestionsClient.fetchData("12345");

        Assertions.assertEquals(reposResponse.title(), "StackOverflow question example");
        Assertions.assertEquals(reposResponse.lastActivityDate(), OffsetDateTime.parse("2021-11-26T08:12:18Z"));
        verify(getRequestedFor(urlEqualTo("/questions/12345?site=stackoverflow")));
    }

    @Test void fetchAnswersTest() {
        String expectedBody =
            "{\"items\":[{\"owner\":{\"account_id\":16867897,\"reputation\":1,\"user_id\":12197151," +
                "\"user_type\":\"registered\",\"profile_image\":\"https://lh3.googleusercontent.com" +
                "/a-/AAuE7mAyMqCV9Djemblc5vF4A_DweeeRKDbGLYN5ge2e=k-s256\",\"display_name\":\"ANISH" +
                "\",\"link\":\"https://stackoverflow.com/users/12197151/anish\"},\"is_accepted\":fal" +
                "se,\"score\":0,\"last_activity_date\":1637914338,\"last_edit_date\":1637914338,\"creat" +
                "ion_date\":1637836546,\"answer_id\":70109583,\"question_id\":29433422,\"content_license" +
                "\":\"CC BY-SA 4.0\"},{\"owner\":{\"account_id\":5001989,\"reputation\":479,\"user_id\":40207" +
                "02,\"user_type\":\"registered\",\"profile_image\":\"https://graph.facebook.com/1040263817/pictu" +
                "re?type=large\",\"display_name\":\"Teodor Hirs\",\"link\":\"https://stackoverflow.com/users/40207" +
                "02/teodor-hirs\"},\"is_accepted\":false,\"score\":9,\"last_activity_date\":1585920981,\"last_edit_d" +
                "ate\":1585920981,\"creation_date\":1585919603,\"answer_id\":61012715,\"question_id\":294334" +
                "22,\"content_license\":\"CC BY-SA 4.0\"},{\"owner\":{\"account_id\":63984,\"reputation\":4983" +
                "2,\"user_id\":189134,\"user_type\":\"moderator\",\"accept_rate\":98,\"profile_image\":\"https://" +
                "i.stack.imgur.com/jFsyb.png?s=256&g=1\",\"display_name\":\"Andy\",\"link\":\"https://stackoverf" +
                "low.com/users/189134/andy\"},\"is_accepted\":true,\"score\":7,\"last_activity_date\":1428263283," +
                "\"last_edit_date\":1428263283,\"creation_date\":1428262782,\"answer_id\":29461438,\"question_id\"" +
                ":29433422,\"content_license\":\"CC BY-SA 3.0\"}],\"has_more\":false,\"quota_max\":300,\"quota_rema" +
                "ining\":291}";

        stubFor(get(urlEqualTo("/questions/12345?site=stackoverflow")).willReturn(aResponse().withHeader(
            "Content-Type",
            "application/json"
        ).withBody(expectedBody)));

        StackOverflowQuestionsClient stackOverflowQuestionsClient = StackOverflowQuestionsClient.builder()
            .webClient(WebClient.builder().baseUrl("http://localhost:8080").build()).build();
        Long answersAmount = stackOverflowQuestionsClient.fetchAnswers("12345");
        Assertions.assertEquals(answersAmount, 3L);
    }
}
