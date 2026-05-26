package ru.practicum.shareit.gatewayItemController;

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
import ru.practicum.item.dto.CommentRequestDto;
import ru.practicum.item.dto.CommentResponseDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemResponseDto;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void createNotValidItemTest() throws Exception {
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


    @Test
    void updateItemWithoutHeaderTest() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Молоток");
        request.setDescription("Красивый");
        request.setAvailable(true);

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Молоток");
        response.setDescription("Красивый");
        response.setAvailable(true);

        when(shareItItemClient.updateItem(eq(123L), eq(1L), any(ItemDto.class))).thenReturn(response);

        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getItemTest() throws Exception {

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Молоток");
        response.setDescription("Красивый");
        response.setAvailable(true);
        when(shareItItemClient.getItem(anyLong())).thenReturn(response);

        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.available").value(response.getAvailable()));


    }

    @Test
    void createCommentWithOutHeader() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setText("Красивый был молоток.");
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Красивый был молоток.");

        when((shareItItemClient.createComment(eq(123L), eq(1L), any(CommentRequestDto.class)))).thenReturn(commentResponseDto);

        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    void createItemTest() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Молоток");
        request.setDescription("Красивый");
        request.setAvailable(true);

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.available").value(response.getAvailable()));


    }

    @Test
    void updateItem() throws Exception {
        ItemDto request = new ItemDto();
        request.setName("Молоток");
        request.setDescription("Красивый");
        request.setAvailable(true);

        ItemDto response = new ItemDto();
        response.setId(1L);
        response.setName("Молоток");
        response.setDescription("Красивый");
        response.setAvailable(true);

        when(shareItItemClient.updateItem(eq(123L), eq(1L), any(ItemDto.class))).thenReturn(response);

        mockMvc.perform(patch("/items/1")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.available").value(response.getAvailable()));
    }

    @Test
    void searchItemTest() throws Exception {
        ItemResponseDto response = new ItemResponseDto();
        response.setName("Молоток");
        response.setDescription("Красивый");
        List<ItemResponseDto> list = new ArrayList<>();
        list.add(response);

        when(shareItItemClient.searchItem(eq(123L), eq("молоток"))).thenReturn(list);

        mockMvc.perform(get("/items/search")
                        .header(Headers.SHARER_USER_ID, "123").param("text", "молоток")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(response.getName()))
                .andExpect(jsonPath("$[0].description").value(response.getDescription()));

    }

    @Test
    void getUserItemListTest() throws Exception {
        ItemResponseDto response = new ItemResponseDto();
        response.setName("Молоток");
        response.setDescription("Красивый");
        List<ItemResponseDto> list = new ArrayList<>();
        list.add(response);

        when(shareItItemClient.getItemListByUserId(eq(123L))).thenReturn(list);

        mockMvc.perform(get("/items")
                        .header(Headers.SHARER_USER_ID, "123").param("text", "молоток")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(response.getName()))
                .andExpect(jsonPath("$[0].description").value(response.getDescription()));

    }

    @Test
    void createComment() throws Exception {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setText("Красивый был молоток.");
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setText("Красивый был молоток.");

        when((shareItItemClient.createComment(eq(123L), eq(1L), any(CommentRequestDto.class)))).thenReturn(commentResponseDto);

        mockMvc.perform(post("/items/1/comment")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentResponseDto.getText()));
    }

    @Test
    void searchItemTestEmptyText() throws Exception {

        List<ItemResponseDto> list = new ArrayList<>();


        when(shareItItemClient.searchItem(eq(123L), eq(""))).thenReturn(list);

        mockMvc.perform(get("/items/search")
                        .header(Headers.SHARER_USER_ID, "123").param(" ", "молоток")
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

    }
}
