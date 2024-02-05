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
    private String category;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int stockQuantity;
    private String imagePath;
    @Builder.Default
    private String delYn="N";

    public void updateItem(String name, String category, int price, int stockQuantity, String imagePath) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imagePath = imagePath;
    }
    public void deleteItem() {
        this.delYn = "Y";
    }

    public void UpdateStockQuantity(int newQuantity) {
        this.stockQuantity = newQuantity;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
