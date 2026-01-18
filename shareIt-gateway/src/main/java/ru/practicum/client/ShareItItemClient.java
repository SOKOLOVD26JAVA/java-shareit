package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Paths;
import ru.practicum.exception.GatewayException;
import ru.practicum.item.dto.CommentRequestDto;
import ru.practicum.item.dto.CommentResponseDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemResponseDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareItItemClient {

    private final RestTemplate restTemplate;
    @Value("${shareit.server.url}")
    private String serverUrl;


    public ItemDto createItem(Long userId, ItemDto itemDto) {
        String url = createUrl(Paths.ITEM);
        log.info("Путь POST запроса {}", url);

        HttpEntity<ItemDto> request = new HttpEntity<>(itemDto, createHeadersWithUserId(userId));
        try {
            ResponseEntity<ItemDto> response = restTemplate.exchange(url, HttpMethod.POST, request, ItemDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        String url = createUrl(Paths.ITEM + "/" + itemId);
        log.info("Путь PATCH запроса {}", url);

        HttpEntity<ItemDto> request = new HttpEntity<>(itemDto, createHeadersWithUserId(userId));
        try {
            ResponseEntity<ItemDto> response = restTemplate.exchange(url, HttpMethod.PATCH, request, ItemDto.class, userId);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }

    }

    public ItemDto getItem(Long itemId) {
        String url = createUrl(Paths.ITEM + "/" + itemId);
        log.info("Путь GET запроса {}", url);
        try {
            return restTemplate.getForObject(url, ItemDto.class, itemId);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public List<ItemResponseDto> getItemListByUserId(Long userId) {
        String url = createUrl(Paths.ITEM);
        log.info("Путь GET запроса {} для List ", url);
        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<List<ItemResponseDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, new ParameterizedTypeReference<List<ItemResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public List<ItemResponseDto> searchItem(Long userId, String text) {
        String url = createUrl(Paths.ITEM + "?text=" + text);
        log.info("Путь GET запроса {} с text ", url);

        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<List<ItemResponseDto>> response = restTemplate.exchange(
                    url, HttpMethod.GET, request, new ParameterizedTypeReference<List<ItemResponseDto>>() {
                    }, text);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public CommentResponseDto createComment(Long userId, Long itemId, CommentRequestDto requestDto) {
        String url = createUrl(Paths.ITEM + "/" + itemId + "/comment");
        log.info("Путь POST запроса {} с comments ", url);

        HttpEntity<CommentRequestDto> request = new HttpEntity<>(requestDto, createHeadersWithUserId(userId));
        try {
            ResponseEntity<CommentResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, CommentResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.ALL));

        return headers;
    }

    private HttpHeaders createHeadersWithUserId(Long userId) {
        HttpHeaders headers = createHeaders();
        if (userId != null) {
            headers.set("X-Sharer-User-Id", userId.toString());
        }
        return headers;
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
