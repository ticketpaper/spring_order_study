package com.example.ordering.order.dto.Request;

import lombok.Data;

import java.util.List;

//@Data
//public class OrderReqDto {
//    private List<Long> itemId;
//    private List<Long> counts;
//}

// 예시데이터
/**
 * {
 *     "ItemId":[1,2], "counts":[10,20]
 * }
 */
@Data
public class OrderReqDto {
    private List<OrderItemReqDto> orderItemReqDtos;

    @Data
    public static class OrderItemReqDto {
        private Long itemId;
        private int count;

    }
}

/**
 * 예시 데이터
 * {
 *     "orderItemReqDto" :[
 *         {"itemId":1,"count":10},
 *         {"itemId":2,"count":20}
 *     ]
 * }
 *
 */