package com.example.ordering.order.dto.Response;

import com.example.ordering.order.domain.Ordering;
import com.example.ordering.order_item.domain.OrderItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
public class OrderResDto {
    private Long Id;
    private String memberEmail;
    private String orderStatus;
    private LocalDateTime createdTime;
    private List<OrderResDto.OrderItemResDto> orderItems;

    @Data
    public static class OrderItemResDto {
        private Long id;
        private String itemName;
        private int count;
    }

    public static OrderResDto toDto(Ordering ordering) {
        OrderResDto orderResDto = new OrderResDto();
        orderResDto.setId(ordering.getId());
        orderResDto.setMemberEmail(ordering.getMember().getEmail());
        orderResDto.setOrderStatus(String.valueOf(ordering.getOrderStatus()));
        orderResDto.setCreatedTime(ordering.getCreatedTime());
        List<OrderResDto.OrderItemResDto> orderItems = new ArrayList<>();
        for (OrderItem orderItem : ordering.getOrderItems()) {
            OrderResDto.OrderItemResDto dto = new OrderResDto.OrderItemResDto();
            dto.setId(orderItem.getId());
            dto.setItemName(orderItem.getItem().getName());
            dto.setCount(orderItem.getQuantity());

            orderItems.add(dto);
        }

        orderResDto.setOrderItems(orderItems);
        return orderResDto;
    }
}
