package com.example.ordering.member.controller;

import com.example.ordering.common.ResponseDto;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.dto.Request.MemberCreateReqDto;
import com.example.ordering.member.dto.Request.MemberLoginReqDto;
import com.example.ordering.member.dto.Response.MemberResponseDto;
import com.example.ordering.member.service.MemberService;
import com.example.ordering.securities.JwtAuthFilter;
import com.example.ordering.securities.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<ResponseDto> MemberCreate(@Valid @RequestBody MemberCreateReqDto memberCreateReqDto) {
        Member member = memberService.Create(memberCreateReqDto);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.CREATED, "member successfully created", member.getId()), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<ResponseDto> MemberLogin(@Valid @RequestBody MemberLoginReqDto memberLoginReqDto) {
        Member member = memberService.Login(memberLoginReqDto);
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwtToken);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.OK, "member successfully login", member_info), HttpStatus.OK
        );
    }

    @GetMapping("/members")
    public List<MemberResponseDto> Members() {
        List<MemberResponseDto> all = memberService.findAll();
        return all;
    }

    @GetMapping("/member/myInfo")
    public MemberResponseDto findMyInfo() {
        return memberService.findMyInfo();
    }

//    @GetMapping("/member/{Id}/order")

//    @GetMapping("/member/myOrders")
}