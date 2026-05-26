package ru.practicum.shareit.gatewayItemRequestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Headers;
import ru.practicum.client.ShareItItemRequestClient;
import ru.practicum.controller.GatewayItemRequestController;
import ru.practicum.itemRequest.dto.ItemRequestDto;
import ru.practicum.itemRequest.dto.ItemRequestWithOutResponseDto;
import ru.practicum.itemRequest.dto.ItemRequestWithResponseDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayItemRequestController.class)
public class GatewayItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShareItItemRequestClient shareItItemRequestClient;

    @Test
    void createItemRequestWithOutHeader() throws Exception {
        ItemRequestWithOutResponseDto responseDto = new ItemRequestWithOutResponseDto();
        responseDto.setDescription("описание");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("описание");

        when(shareItItemRequestClient.createItemRequest(eq(123L), any(ItemRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getUserItemRequestsWithOutHeader() throws Exception {
        List<ItemRequestWithResponseDto> requestList = new ArrayList<>();
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");
        requestList.add(response);
        when(shareItItemRequestClient.getUserItemRequests(eq(123L))).thenReturn(requestList);

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemRequestByIdWithOutHeader() throws Exception {
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");

        when(shareItItemRequestClient.getItemRequestById(eq(123L), eq(1L))).thenReturn(response);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getAllItemRequestsWithOutHeader() throws Exception {
        List<ItemRequestWithResponseDto> requestList = new ArrayList<>();
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        response.setDescription("описание");
        requestList.add(response);
        when(shareItItemRequestClient.getAllItemRequests(eq(123L))).thenReturn(requestList);

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

    @Test
    void createItemRequest() throws Exception {
        ItemRequestWithOutResponseDto responseDto = new ItemRequestWithOutResponseDto();
        responseDto.setDescription("описание");

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("описание");

        when(shareItItemRequestClient.createItemRequest(eq(123L), any(ItemRequestDto.class))).thenReturn(responseDto);

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
        when(shareItItemRequestClient.getUserItemRequests(eq(123L))).thenReturn(requestList);

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

        when(shareItItemRequestClient.getItemRequestById(eq(123L), eq(1L))).thenReturn(response);

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
        when(shareItItemRequestClient.getAllItemRequests(eq(123L))).thenReturn(requestList);

        mockMvc.perform(get("/requests/all")
                        .header(Headers.SHARER_USER_ID, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(response.getDescription()));


    }

}
