package ru.practicum.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import ru.practicum.Paths;
import ru.practicum.exception.GatewayException;
import ru.practicum.user.dto.UserDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareItUserClient {

    private final RestTemplate restTemplate;
    @Value("${shareit.server.url}")
    private String serverUrl;

    public UserDto createUser(UserDto userDto) {

        String url = createUrl(Paths.USER);
        log.info("Путь POST запроса {}", url);

        HttpEntity<UserDto> request = new HttpEntity<>(userDto, createHeaders());
        try {
            ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.POST, request, UserDto.class);

            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }

    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        String url = createUrl(Paths.USER + "/" + userId);
        log.info("Путь PATH запроса {}", url);

        HttpEntity<UserDto> request = new HttpEntity<>(userDto, createHeaders());
        try {
            ResponseEntity<UserDto> response = restTemplate.exchange(url, HttpMethod.PATCH, request, UserDto.class, userId);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public UserDto getUserById(Long userId) {
        String url = createUrl(Paths.USER + "/" + userId);
        log.info("Путь GET запроса {}", url);

        try {
            return restTemplate.getForObject(url, UserDto.class, userId);
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public void deleteUserById(Long userId) {
        String url = createUrl(Paths.USER + "/" + userId);
        log.info("Путь DELETE запроса {}", url);

        try {
            restTemplate.delete(url, userId);
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

    private String createUrl(String path) {
        return serverUrl + path;
    }


}
