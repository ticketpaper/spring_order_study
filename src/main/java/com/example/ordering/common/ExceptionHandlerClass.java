package com.example.ordering.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerClass {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException e) {
        log.error("Handler EntityNotFoundException message : " + e.getMessage());
        return ErrorResponseDto.errorResMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArguHandler(IllegalArgumentException e) {
        log.error("Handler IllegalArgumentException message : " + e.getMessage());
        return ErrorResponseDto.errorResMessage(HttpStatus.BAD_REQUEST,e.getMessage());
    }

}
