package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> annotationHandler(MethodArgumentNotValidException e, WebRequest request) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < e.getBindingResult().getErrorCount(); i++) {
//            sb.append(e.getBindingResult().getFieldErrors().get(i).getDefaultMessage() + " ");
//        }
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage().toString(), request.getDescription(false));
//        e.getBindingResult().getFieldErrors().forEach(fieldError ->
//                log.warn("Ошибка валидации поля: {}", fieldError.getField()));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GatewayException.class)
    public ResponseEntity<ErrorResponse> notFoundHandler(GatewayException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(e.getStatus().value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(error, e.getStatus());
    }

}
