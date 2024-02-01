package com.example.ordering.securities;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public String createToken(String email, String role) {
//        claims : 클레임은 토큰 사용자의 대한 속성이나 데이터 포함, 주로 페이로드를 의미
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))
                .signWith(SignatureAlgorithm.HS256, "mySecretKey")
                .compact();

        return token;

//        아래와 같이 가능
//        JwtBuilder jwtBuilder = Jwts.builder();
//        jwtBuilder.setClaims(claims);
//        jwtBuilder.setIssuedAt(now);
//        jwtBuilder.setExpiration(new Date(now.getTime() + 30 * 60 * 1000L));
//        jwtBuilder.signWith(SignatureAlgorithm.HS256, "mySecretKey");
//        return jwtBuilder.compact();
    }
}
