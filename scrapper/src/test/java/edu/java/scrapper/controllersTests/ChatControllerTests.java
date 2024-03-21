package edu.java.scrapper.controllersTests;

import edu.java.scrapper.controllers.ChatController;
import edu.java.scrapper.services.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ChatControllerTests {
    private MockMvc mockMvc;

    @Mock
    ChatService chatService;

    @InjectMocks
    ChatController chatController;

    @BeforeEach
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatController).build();
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