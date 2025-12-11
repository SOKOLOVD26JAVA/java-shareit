package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(ru.practicum.shareit.exception.ValidationException.class)
    public ResponseEntity<ErrorResponse> validationHandler(ValidationException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> annotationHandler(MethodArgumentNotValidException e, WebRequest request) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < e.getBindingResult().getErrorCount(); i++) {
            sb.append(e.getBindingResult().getFieldErrors().get(i).getDefaultMessage() + " ");
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), sb.toString(), request.getDescription(false));
        e.getBindingResult().getFieldErrors().forEach(fieldError ->
                log.warn("Ошибка валидации поля: {}", fieldError.getField()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(NotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> missingHeaderHandler(Exception e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Пропущен обязательный заголовок.", request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
