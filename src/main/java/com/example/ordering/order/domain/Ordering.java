package com.example.ordering.order.domain;

import com.example.ordering.common.BaseEntity;
import com.example.ordering.member.domain.Address;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.domain.Role;
import com.example.ordering.order_item.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Ordering extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    @OneToMany(mappedBy = "ordering", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();


    @Builder
    public Ordering(Member member) {
        this.member = member;
    }

    public void orderCancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }
}
