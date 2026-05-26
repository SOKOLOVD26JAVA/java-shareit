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
import ru.practicum.itemRequest.dto.ItemRequestDto;
import ru.practicum.itemRequest.dto.ItemRequestWithOutResponseDto;
import ru.practicum.itemRequest.dto.ItemRequestWithResponseDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareItItemRequestClient {

    private final RestTemplate restTemplate;
    @Value("${shareit.server.url}")
    private String serverUrl;

    public ItemRequestWithOutResponseDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        String url = createUrl(Paths.REQUEST);
        log.info("Путь POST запроса {}", url);

        HttpEntity<ItemRequestDto> request = new HttpEntity<>(itemRequestDto, createHeadersWithUserId(userId));

        try {
            ResponseEntity<ItemRequestWithOutResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, ItemRequestWithOutResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public List<ItemRequestWithResponseDto> getUserItemRequests(Long userId) {
        String url = createUrl(Paths.REQUEST);
        log.info("Путь GET запроса {}", url);

        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));

        try {
            ResponseEntity<List<ItemRequestWithResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<ItemRequestWithResponseDto>>() {
                    });
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public ItemRequestWithResponseDto getItemRequestById(Long userId, Long requestId) {
        String url = createUrl(Paths.REQUEST + "/" + requestId);
        log.info("Путь GET запроса {} c requestId", url);
        HttpEntity<ItemRequestDto> request = new HttpEntity<>(createHeadersWithUserId(userId));

        try {
            ResponseEntity<ItemRequestWithResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, ItemRequestWithResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }

    }

    public List<ItemRequestWithResponseDto> getAllItemRequests(Long userId) {
        String url = createUrl(Paths.REQUEST + "/all");
        log.info("Путь GET all запроса {}", url);

        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));

        try {
            ResponseEntity<List<ItemRequestWithResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<ItemRequestWithResponseDto>>() {
                    });
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
        headers.set("X-Sharer-User-Id", userId.toString());
        return headers;
    }

    private String createUrl(String path) {
        return serverUrl + path;
    }
}
