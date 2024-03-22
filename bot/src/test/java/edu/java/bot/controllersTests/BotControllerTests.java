package edu.java.bot.controllersTests;

import edu.java.bot.controllers.BotController;
import edu.java.bot.suppliers.MessageSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BotController.class)
public class BotControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    MessageSupplier messageSupplier;

    @BeforeEach
    public void initialize() {
        doNothing().when(messageSupplier).send(anyLong(), anyString());
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
