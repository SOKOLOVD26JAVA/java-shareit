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
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.exception.GatewayException;


import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShareItBookingClient {

    private final RestTemplate restTemplate;
    @Value("${shareit.server.url}")
    private String serverUrl;


    public BookingResponseDto createBooking(Long userId, BookingRequestDto dto) {
        String url = createUrl(Paths.BOOKING);
        log.info("Путь POST запроса {}", url);
        HttpEntity<BookingRequestDto> request = new HttpEntity<>(dto, createHeadersWithUserId(userId));
        try {
            ResponseEntity<BookingResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.POST, request, BookingResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public BookingResponseDto bookingApprove(Long userId, Long bookingId, Boolean approved) {
        String url = createUrl(Paths.BOOKING + "/" + bookingId + "?approved=" + approved);
        log.info("Путь PATCH запроса {} с approved", url);
        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<BookingResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.PATCH, request, BookingResponseDto.class, approved);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }

    }

    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        String url = createUrl(Paths.BOOKING + "/" + bookingId);
        log.info("Путь GET запроса {}", url);
        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<BookingResponseDto> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, BookingResponseDto.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public List<BookingResponseDto> getBookingByIdAndStatus(Long userId, String state) {
        String url = createUrl(Paths.BOOKING + "?state=" + state);
        log.info("Путь GET запроса {} с state", url);
        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<List<BookingResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<BookingResponseDto>>() {
                    }, state);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new GatewayException((HttpStatus) e.getStatusCode(), e.getMessage());
        }
    }

    public List<BookingResponseDto> getBookingByOwnerAndStatus(Long userId, String state) {
        String url = createUrl(Paths.BOOKING + "/owner" + "?state=" + state);
        log.info("Путь GET запроса {} с state и owner", url);
        HttpEntity<Void> request = new HttpEntity<>(createHeadersWithUserId(userId));
        try {
            ResponseEntity<List<BookingResponseDto>> response = restTemplate
                    .exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<BookingResponseDto>>() {
                    }, state);
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
