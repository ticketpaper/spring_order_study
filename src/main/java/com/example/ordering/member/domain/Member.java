package com.example.ordering.member.domain;

import com.example.ordering.common.BaseEntity;
import com.example.ordering.member.dto.Request.MemberCreateReqDto;
import com.example.ordering.order.domain.Ordering;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Member extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Ordering> ordering;

    public static Member toEntity(MemberCreateReqDto memberCreateReqDto) {
        Address address = new Address(memberCreateReqDto.getCity(), memberCreateReqDto.getStreet(), memberCreateReqDto.getZipcode());

        Member member = Member.builder()
                .name(memberCreateReqDto.getName())
                .email(memberCreateReqDto.getEmail())
                .password(memberCreateReqDto.getPassword())
                .address(address)
                .role(Role.USER)
                .build();

        return member;
    }
}
