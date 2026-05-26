package ru.practicum.shareit.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.shareit.exception.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private WebRequest webRequest = new ServletWebRequest(request);

    @Test
    void validationHandlerTest() {
        ValidationException exception = new ValidationException("Ошибка валидации");
        ResponseEntity<ErrorResponse> response = handler.validationHandler(exception, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Ошибка"));
    }

    @Test
    void notFoundHandlerTest() {
        NotFoundException exception = new NotFoundException("Не найдено");
        ResponseEntity<ErrorResponse> response = handler.notFoundHandler(exception, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void bookingValidationHandlerTest() {
        BookingValidationException exception = new BookingValidationException("Бронирование не валидно");
        ResponseEntity<ErrorResponse> response = handler.bookingValidationHandler(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void missingHeaderHandlerTest() {
        Exception exception = new Exception("Пропущен заголовок");
        ResponseEntity<ErrorResponse> response = handler.missingHeaderHandler(exception, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
