package ru.practicum.shareit.validationTest.gatewayItemController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Headers;
import ru.practicum.client.ShareItItemClient;
import ru.practicum.controller.GatewayItemController;
import ru.practicum.item.dto.ItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayItemController.class)
public class GatewayItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShareItItemClient shareItItemClient;

    @Test
    void createItemTest() throws Exception {
        ItemDto request = new ItemDto();

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Молоток");
        response.setDescription("Красивый");
        response.setAvailable(true);

        when(shareItItemClient.createItem(eq(123L), any(ItemDto.class))).thenReturn(response);
        mockMvc.perform(post("/items")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }
}
