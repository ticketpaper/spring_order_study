package com.example.ordering.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
