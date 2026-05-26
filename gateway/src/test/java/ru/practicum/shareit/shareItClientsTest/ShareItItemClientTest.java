package ru.practicum.shareit.shareItClientsTest;

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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Paths;
import ru.practicum.client.ShareItItemClient;
import ru.practicum.exception.GatewayException;
import ru.practicum.item.dto.CommentRequestDto;
import ru.practicum.item.dto.CommentResponseDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemResponseDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class ShareItItemClientTest {

    @Autowired
    private ShareItItemClient client;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${shareit.server.url}")
    private String serverUrl;

    @Test
    void createItemTest() {
        ItemDto request = new ItemDto();
        request.setName("Тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM), eq(HttpMethod.POST), any(HttpEntity.class), eq(ItemDto.class)))
                .thenReturn(new ResponseEntity<>(request, HttpStatus.OK));

        client.createItem(1L, request);
    }

    @Test
    void updateItemTest() {
        ItemDto request = new ItemDto();
        request.setName("Тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "/1"), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(ItemDto.class), any(Long.class)))
                .thenReturn(new ResponseEntity<>(request, HttpStatus.OK));

        client.updateItem(1L, 1L, request);
    }

    @Test
    void getItemTest() {

        when(restTemplate.getForObject(eq(serverUrl + Paths.ITEM + "/1"), eq(ItemDto.class), any(Long.class)))
                .thenReturn(new ItemDto());

        client.getItem(1L);
    }

    @Test
    void getItemListByUserIdTest() {

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        client.getItemListByUserId(1L);
    }

    @Test
    void searchItemTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "?text=" + "text"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        client.searchItem(1L, "text");
    }

    @Test
    void createCommentTest() {
        CommentRequestDto request = new CommentRequestDto();
        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "/1/comment"), eq(HttpMethod.POST), any(HttpEntity.class), eq(CommentResponseDto.class)))
                .thenReturn(new ResponseEntity<>(new CommentResponseDto(), HttpStatus.OK));

        client.createComment(1L, 1L, request);
    }

    @Test
    void createItemExceptionTest() {
        ItemDto request = new ItemDto();
        request.setName("Тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM), eq(HttpMethod.POST), any(HttpEntity.class), eq(ItemDto.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.createItem(1L, request);
        });
    }

    @Test
    void updateItemExceptionTest() {
        ItemDto request = new ItemDto();
        request.setName("Тест");

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "/1"), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(ItemDto.class), any(Long.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.updateItem(1L, 1L, request);
        });
    }

    @Test
    void getItemExceptionTest() {

        when(restTemplate.getForObject(eq(serverUrl + Paths.ITEM + "/1"), eq(ItemDto.class), any(Long.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.getItem(1L);
        });
    }

    @Test
    void getItemListByUserIdExceptionTest() {

        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.getItemListByUserId(1L);
        });
    }

    @Test
    void searchItemExceptionTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "?text=" + "text"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.searchItem(1L, "text");
        });
    }

    @Test
    void createCommentExceptionTest() {
        CommentRequestDto request = new CommentRequestDto();
        when(restTemplate.exchange(eq(serverUrl + Paths.ITEM + "/1/comment"), eq(HttpMethod.POST), any(HttpEntity.class), eq(CommentResponseDto.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.createComment(1L, 1L, request);
        });
    }
}
