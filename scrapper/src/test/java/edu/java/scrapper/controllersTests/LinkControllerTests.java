package edu.java.scrapper.controllersTests;

import edu.java.scrapper.controllers.LinkController;
import edu.java.scrapper.services.LinkService;
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
public class LinkControllerTests {
    private MockMvc mockMvc;

    @Mock
    LinkService linkService;

    @InjectMocks
    LinkController linkController;

    @BeforeEach
    public void initialize() {
        mockMvc = MockMvcBuilders.standaloneSetup(linkController).build();
    }

    @Test
    public void addLink() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
            .post("/links")
            .header("Tg-Chat-Id", 1L)
            .accept(MediaType.APPLICATION_JSON)
            .content("{\"url\":\"testurl\"}")
            .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isOk());
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
}
