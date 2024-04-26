package edu.java.bot.controllersTests;

import edu.java.bot.controllers.BotController;
import edu.java.bot.services.NotificationService;
import edu.java.bot.suppliers.MessageSupplier;
import edu.java.bot.utils.BucketGrabber;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BotController.class)
public class BotControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    NotificationService notificationService;

    @MockBean
    BucketGrabber bucketGrabber;

    @BeforeEach
    public void initialize() {
        doNothing().when(notificationService).sendUpdates(any());
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
    public void sendMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/updates")
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\", \"description\":\"test\", \"tgChatIds\":[]}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().isOk());
        mockMvc.perform(request).andExpect(status().is(400));
    }
}
