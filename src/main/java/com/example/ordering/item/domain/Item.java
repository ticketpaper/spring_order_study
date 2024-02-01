package com.example.ordering.item.domain;

import com.example.ordering.common.BaseEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Item extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    @Setter
    private int stockQuantity;
    private String imagePath;
}
