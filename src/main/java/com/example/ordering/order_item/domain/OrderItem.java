package com.example.ordering.order_item.domain;

import com.example.ordering.common.BaseEntity;
import com.example.ordering.item.domain.Item;
import com.example.ordering.order.domain.OrderStatus;
import com.example.ordering.order.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class OrderItem extends BaseEntity {
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Ordering ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;


}
