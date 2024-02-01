package com.example.ordering.member.dto.Request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberCreateReqDto {
    @NotEmpty(message = "name is essential")
    private String name;

    @Email(message = "email is not valid")
    @NotEmpty(message = "email is essential")
    private String email;

    @Size(min = 4, message = "minimum length is 4")
    @NotEmpty(message = "password is essential")
    private String password;

    private String city;
    private String street;
    private String zipcode;
}
