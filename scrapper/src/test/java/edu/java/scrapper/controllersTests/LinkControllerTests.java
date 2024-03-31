package edu.java.scrapper.controllersTests;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.scrapper.controllers.LinkController;
import edu.java.scrapper.services.LinkService;
import edu.java.scrapper.utils.BucketGrabber;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.Duration;
import java.util.ArrayList;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
@WireMockTest(httpPort = 8080)
public class LinkControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LinkService linkService;

    @MockBean
    BucketGrabber bucketGrabber;

    @BeforeEach
    public void initialize() {
        doNothing().when(linkService).add(anyLong(), any());
        doNothing().when(linkService).remove(anyLong(), any());
        Mockito.when(linkService.listAll(anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(bucketGrabber.grabBucket(anyString())).thenReturn(
            Bucket.builder()
                .addLimit(
                    Bandwidth.classic(
                        3,
                        Refill.intervally(1, Duration.ofMinutes(1)))
                )
                .build()
        );
    }

    @Test
    public void addLink() throws Exception {
        stubFor(get(urlEqualTo("/repos/martynovvladislav/tinkoff-java-backend-course-2024"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("")
            )
        );
        RequestBuilder request = MockMvcRequestBuilders
            .post("/links")
            .header("Tg-Chat-Id", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"link\":\"https://github.com/martynovvladislav/tinkoff-java-backend-course-2024\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void addBadFormatLink() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/links")
            .header("Tg-Chat-Id", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"link\":\"https://badlink.com\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteLink() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .delete("/links")
            .header("Tg-Chat-Id", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void getLinks() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .get("/links")
            .header("Tg-Chat-Id", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void bucketTest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .get("/links")
            .header("Tg-Chat-Id", 1L)
            .header("X-Forwarded-For", "0.0.0.0")
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().is(400));
    }
}
