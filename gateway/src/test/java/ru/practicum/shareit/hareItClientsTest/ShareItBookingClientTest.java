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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.Paths;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.client.ShareItBookingClient;
import ru.practicum.exception.GatewayException;
import ru.practicum.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ShareItBookingClientTest {
    @Autowired
    private ShareItBookingClient client;

    @MockBean
    private RestTemplate restTemplate;

    @Value("${shareit.server.url}")
    private String serverUrl;

    @Test
    void createBookingTest() {
        BookingRequestDto request = new BookingRequestDto();
        request.setStart(LocalDateTime.now());
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());


        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING), eq(HttpMethod.POST), any(HttpEntity.class), eq(BookingResponseDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
        client.createBooking(1L, request);
    }

    @Test
    void bookingApprovedTest() {
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/" + "1" + "?approved=" + true), eq(HttpMethod.PATCH),
                any(HttpEntity.class), eq(BookingResponseDto.class), any(Boolean.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        client.bookingApprove(1L, 1L, true);
    }

    @Test
    void getBookingTest() {
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/" + "1"), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(BookingResponseDto.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        client.getBooking(1L, 1L);
    }

    @Test
    void getBookingByIdAndStatusTest() {

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "?state=ALL"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        client.getBookingByIdAndStatus(1L, "ALL");
    }

    @Test
    void getBookingByOwnerAndStatusTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/owner?state=ALL"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        client.getBookingByOwnerAndStatus(1L, "ALL");
    }

    @Test
    void createBookingExceptionTest() {
        BookingRequestDto request = new BookingRequestDto();
        request.setStart(LocalDateTime.now());
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());


        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING), eq(HttpMethod.POST), any(HttpEntity.class), eq(BookingResponseDto.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.createBooking(1L, request);
        });
    }

    @Test
    void bookingApprovedExceptionTest() {
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/" + "1" + "?approved=" + true), eq(HttpMethod.PATCH),
                any(HttpEntity.class), eq(BookingResponseDto.class), any(Boolean.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.bookingApprove(1L, 1L, true);
        });
    }

    @Test
    void getBookingExceptionTest() {
        BookingResponseDto response = new BookingResponseDto();
        response.setStart(LocalDateTime.now());

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/" + "1"), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(BookingResponseDto.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.getBooking(1L, 1L);
        });
    }

    @Test
    void getBookingByIdAndStatusExceptionTest() {

        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "?state=ALL"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.getBookingByIdAndStatus(1L, "ALL");
        });
    }

    @Test
    void getBookingByOwnerAndStatusExceptionTest() {
        when(restTemplate.exchange(eq(serverUrl + Paths.BOOKING + "/owner?state=ALL"), eq(HttpMethod.GET),
                any(HttpEntity.class), ArgumentMatchers.<ParameterizedTypeReference<List<ItemResponseDto>>>any(), any(String.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "ошибка"));

        assertThrows(GatewayException.class, () -> {
            client.getBookingByOwnerAndStatus(1L, "ALL");
        });
    }

}
