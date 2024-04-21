package edu.java.scrapper.controllersTests;

import edu.java.scrapper.controllers.ChatController;
import edu.java.scrapper.services.ChatService;
import edu.java.scrapper.utils.BucketGrabber;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.Duration;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ChatService chatService;

    @MockBean
    BucketGrabber bucketGrabber;

    @BeforeEach
    void initialize() {
        doNothing().when(chatService).register(anyLong());
        doNothing().when(chatService).unregister(anyLong());
        Mockito.when(bucketGrabber.grabBucket(anyString())).thenReturn(
            Bucket.builder()
                .addLimit(
                    Bandwidth.classic(
                        10,
                        Refill.intervally(10, Duration.ofMinutes(1)))
                )
                .build()
        );
    }

    @Test
    public void registerChat() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/tg-chat/1")
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"id\":1}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }

    @Test
    public void deleteChat() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .delete("/tg-chat/1")
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"id\":1}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }
}
