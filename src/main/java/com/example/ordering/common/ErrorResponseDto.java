package com.example.ordering.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseDto {
    public static ResponseEntity<Map<String, Object>> errorResMessage(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", String.valueOf(status.value()));
        body.put("error message", message);
        return new ResponseEntity<>(body, status);
    }
}
