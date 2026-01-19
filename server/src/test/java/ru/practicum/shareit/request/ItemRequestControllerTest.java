package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Headers;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithOutResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void createItemRequest() throws Exception {
        ItemRequestWithOutResponseDto responseDto = new ItemRequestWithOutResponseDto();
        responseDto.setDescription("описание");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("описание");

        when(itemRequestService.createItemRequest(eq(123L),any(ItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(responseDto.getDescription()));
    }

    @Test
    void getUserItemRequests() throws Exception {
        List<ItemRequestWithResponseDto> requestList = new ArrayList<>();
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");
        requestList.add(response);
        when(itemRequestService.getUserItemRequests(eq(123L))).thenReturn(requestList);

        mockMvc.perform(get("/requests")
                        .header(Headers.SHARER_USER_ID, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(response.getDescription()));


    }

    @Test
    void getItemRequestById() throws Exception {
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");

        when(itemRequestService.getItemRequestById(eq(123L),eq(1L))).thenReturn(response);

        mockMvc.perform(get("/requests/1")
                        .header(Headers.SHARER_USER_ID, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(response.getDescription()));

    }

    @Test
    void getAllItemRequests() throws Exception {
        List<ItemRequestWithResponseDto> requestList = new ArrayList<>();
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");
        requestList.add(response);
        when(itemRequestService.getAllItemRequests(eq(123L))).thenReturn(requestList);

        mockMvc.perform(get("/requests/all")
                        .header(Headers.SHARER_USER_ID, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(response.getDescription()));


    }
}
