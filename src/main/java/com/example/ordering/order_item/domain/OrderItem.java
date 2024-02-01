package com.example.ordering.order_item.domain;

import com.example.ordering.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class OrderItem extends BaseEntity {
    private int quantity;
}
