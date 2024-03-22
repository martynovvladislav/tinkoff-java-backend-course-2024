package edu.java.scrapper.controllersTests;

import edu.java.scrapper.controllers.LinkController;
import edu.java.scrapper.services.LinkService;
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
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LinkController.class)
public class LinkControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LinkService linkService;

    @BeforeEach
    public void initialize() {
        doNothing().when(linkService).add(anyLong(), any());
        doNothing().when(linkService).remove(anyLong(), any());
        Mockito.when(linkService.listAll(anyLong())).thenReturn(new ArrayList<>());
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
