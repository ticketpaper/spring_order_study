package com.example.ordering.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
public class ResponseDto {
    private HttpStatus status;
    private String message;
    private Object Result;
}
