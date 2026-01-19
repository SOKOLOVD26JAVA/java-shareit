package ru.practicum.shareit.hareItClientsTest;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Paths;
import ru.practicum.client.ShareItItemRequestClient;
import ru.practicum.item.dto.ItemResponseDto;
import ru.practicum.itemRequest.dto.ItemRequestDto;
import ru.practicum.itemRequest.dto.ItemRequestWithOutResponseDto;
import ru.practicum.itemRequest.dto.ItemRequestWithResponseDto;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ShareItItemRequestClientTest {
    @Autowired
    private ShareItItemRequestClient client;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${shareit.server.url}")
    private String serverUrl;

    @Test
    void createItemRequestTest() {
        ItemRequestWithOutResponseDto response = new ItemRequestWithOutResponseDto();
        response.setDescription("тест");
        ItemRequestDto request = new ItemRequestDto();
        request.setDescription("тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.REQUEST),
                eq(HttpMethod.POST), any(HttpEntity.class), eq(ItemRequestWithOutResponseDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        client.createItemRequest(1L, request);
    }

    @Test
    void getUserItemRequestsTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.REQUEST), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        client.getUserItemRequests(1L);
    }

    @Test
    void getItemRequestById() {
        ItemRequestWithResponseDto response = new ItemRequestWithResponseDto();
        when(restTemplate.exchange(eq(serverUrl + Paths.REQUEST + "/1"),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(ItemRequestWithResponseDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        client.getItemRequestById(1L, 1L);
    }

    @Test
    void getAllItemRequestsTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.REQUEST + "/all"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        client.getAllItemRequests(1L);
    }
}
