package com.example.ordering.member.controller;

import com.example.ordering.common.CommonResponseDto;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.dto.Request.MemberCreateReqDto;
import com.example.ordering.member.dto.Request.MemberLoginReqDto;
import com.example.ordering.member.dto.Response.MemberResponseDto;
import com.example.ordering.member.service.MemberService;
import com.example.ordering.order.dto.Response.OrderResDto;
import com.example.ordering.order.service.OrderService;
import com.example.ordering.securities.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public MemberController(MemberService memberService, OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponseDto> MemberCreate(@Valid MemberCreateReqDto memberCreateReqDto) {
        Member member = memberService.Create(memberCreateReqDto);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.CREATED, "member successfully created", member.getId()), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponseDto> MemberLogin(@Valid @RequestBody MemberLoginReqDto memberLoginReqDto) {
        Member member = memberService.Login(memberLoginReqDto);
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwtToken);
        return new ResponseEntity<>(new CommonResponseDto(HttpStatus.OK, "member successfully login", member_info), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponseDto> Members() {
        List<MemberResponseDto> all = memberService.findAll();
        return all;
    }

    @GetMapping("/member/myInfo")
    public MemberResponseDto findMyInfo() {
        return memberService.findMyInfo();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/order")
    public List<OrderResDto> findMemberOrder(@PathVariable Long id) {
        return orderService.findByMember(id);
    }

    @GetMapping("/member/myOrders")
    public List<OrderResDto> myOrder() {
        return orderService.findMyOrders();
    }


}