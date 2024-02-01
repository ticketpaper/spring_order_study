package com.example.ordering.order.domain;

import com.example.ordering.common.BaseEntity;
import com.example.ordering.member.domain.Address;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Ordering extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

}
