package com.example.ordering.member.service;

import com.example.ordering.member.domain.Address;
import com.example.ordering.member.domain.Member;
import com.example.ordering.member.domain.Role;
import com.example.ordering.member.dto.Request.MemberCreateReqDto;
import com.example.ordering.member.dto.Request.MemberLoginReqDto;
import com.example.ordering.member.dto.Response.MemberResponseDto;
import com.example.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member Create(MemberCreateReqDto memberCreateReqDto) {
        memberCreateReqDto.setPassword(passwordEncoder.encode(memberCreateReqDto.getPassword()));
        Member member = Member.toEntity(memberCreateReqDto);
        memberRepository.save(member);
        return member;
    }



    public Member Login(MemberLoginReqDto memberLoginReqDto) {
//        email 존재 여부 체크
        Member member = memberRepository.findByEmail(memberLoginReqDto.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 이메일입니다."));
//        password 일치 여부 체크
        if(!passwordEncoder.matches(memberLoginReqDto.getPassword(), member.getPassword())){
            throw new IllegalArgumentException("비밀번호가 다릅니다.");
        }
        return member;
    }


    public MemberResponseDto findMyInfo() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("존재 하지 않는 이메일입니다."));

        return MemberResponseDto.toMemberResponseDto(member);
    }

    public List<MemberResponseDto> findAll() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(MemberResponseDto::toMemberResponseDto).collect(Collectors.toList());
    }
}
