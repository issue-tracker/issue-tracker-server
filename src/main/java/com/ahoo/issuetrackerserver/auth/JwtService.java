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

    public void validateAccessToken(AccessToken accessToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(JwtGenerator.SECRET_KEY)
                .build()
                .parseClaimsJws(accessToken.getAccessToken())
                .getBody();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다.", e);
        }
    }
}
