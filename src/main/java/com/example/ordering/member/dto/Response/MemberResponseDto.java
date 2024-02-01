package com.example.ordering.member.dto.Response;


import com.example.ordering.member.domain.Address;
import com.example.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResponseDto {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;

    public static MemberResponseDto toMemberResponseDto(Member member) {
        MemberResponseDtoBuilder builder = MemberResponseDto.builder();
        builder.id(member.getId());
        builder.name(member.getName());
        builder.email(member.getEmail());
        builder.orderCount(member.getOrdering().size());
        Address address = member.getAddress();
        if (address != null) {
            builder.city(address.getCity());
            builder.street(address.getStreet());
            builder.zipcode(address.getZipcode());
        }

        return builder.build();
    }
}
