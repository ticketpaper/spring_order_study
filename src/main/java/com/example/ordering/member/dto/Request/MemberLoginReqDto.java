package com.example.ordering.member.dto.Request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class MemberLoginReqDto {
    @Email(message = "email is not valid")
    @NotEmpty(message = "email is essential")
    private String email;

    @Size(min = 4, message = "minimum length is 4")
    @NotEmpty(message = "password is essential")
    private String password;
}
