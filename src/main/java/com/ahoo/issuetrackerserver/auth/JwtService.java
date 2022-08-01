package com.ahoo.issuetrackerserver.auth;

import com.ahoo.issuetrackerserver.auth.jwt.JwtGenerator;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    public void validateToken(JwtToken token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(JwtGenerator.SECRET_KEY)
                .build()
                .parseClaimsJws(token.getToken())
                .getBody();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다.", e);
        }
    }

    public Long extractMemberId(JwtToken token) {
        return Jwts.parserBuilder()
            .setSigningKey(JwtGenerator.SECRET_KEY)
            .build()
            .parseClaimsJws(token.getToken())
            .getBody()
            .get("memberId", Long.class);
    }
}
