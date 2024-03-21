package edu.java.bot.controllersTests;

import edu.java.bot.controllers.BotController;
import edu.java.bot.suppliers.MessageSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BotControllerTests {
    private MockMvc mockMvc;

    @Mock
    MessageSupplier messageSupplier;

    @InjectMocks
    BotController botController;

    @BeforeEach
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(botController).build();
    }

    @Test
    public void sendMessage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/updates")
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\", \"description\":\"test\", \"tgChatIds\":[]}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
    }
}
