package com.example.ordering.securities;

import com.example.ordering.common.ErrorResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");
            if (bearerToken != null) {
                if (!bearerToken.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationException("token의 형식이 맞지 않습니다.");
                }
//            bearer 토큰에서 토큰 값만 추출
                String token = bearerToken.substring(7);
//                토큰 검증 및 claims 추출, parse 하는것 자체가 검증
//                Authentication 객체를 생성하기위한 UserDetails 생성
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody(); //검증
//                권한이 여러개 일수도 있어서 리스트로 받음
                List<GrantedAuthority> authorities = new ArrayList<>();
//                ROLE_권한 이 패턴으로 스프링에서 기본적으로 권한 체크
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException e) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.getWriter().write(ErrorResponseDto.errorResMessage(HttpStatus.UNAUTHORIZED, e.getMessage()).toString());
        }
    }
}

