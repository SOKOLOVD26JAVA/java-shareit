package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.exception.ErrorResponse;
import ru.practicum.exception.GatewayException;
import ru.practicum.exception.GlobalExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionsTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private WebRequest webRequest = new ServletWebRequest(request);

    @Test
    void notFoundHandlerTest() {
        GatewayException exception = new GatewayException(HttpStatus.CONFLICT, "Ошибка");
        ResponseEntity<ErrorResponse> response = handler.notFoundHandler(exception, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Ошибка"));
    }
}
