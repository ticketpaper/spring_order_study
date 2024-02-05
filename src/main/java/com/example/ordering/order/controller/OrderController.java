package com.example.ordering.order.controller;

import com.example.ordering.common.CommonResponseDto;
import com.example.ordering.order.domain.Ordering;
import com.example.ordering.order.dto.Request.OrderReqDto;
import com.example.ordering.order.dto.Response.OrderResDto;
import com.example.ordering.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order/create")
    public ResponseEntity<CommonResponseDto> orderCreate(@RequestBody OrderReqDto orderReqDto) {
        Ordering ordering = orderService.create(orderReqDto);
        return new ResponseEntity<>(new CommonResponseDto
                (HttpStatus.OK, "order successfully create", ordering.getId())
                , HttpStatus.OK);
    }

    @DeleteMapping("/order/{id}/cancel")
    public ResponseEntity<CommonResponseDto> orderCancel(@PathVariable Long id) {
        Ordering ordering = orderService.cancel(id);
        return new ResponseEntity<>(new CommonResponseDto
                (HttpStatus.OK, "order successfully cancel", ordering.getId())
                , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<OrderResDto> orderList() {
        List<OrderResDto> all = orderService.findAll();
        return all;
    }
}
