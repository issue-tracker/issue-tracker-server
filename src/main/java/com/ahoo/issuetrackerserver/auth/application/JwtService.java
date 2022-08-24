package com.ahoo.issuetrackerserver.auth.application;

import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtConstant;
import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.JwtToken;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.common.exception.UnAuthorizedException;
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
                .setSigningKey(JwtConstant.SECRET_KEY)
                .build()
                .parseClaimsJws(token.getToken())
                .getBody();
        } catch (SignatureException | ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new UnAuthorizedException(ErrorType.INVALID_TOKEN, new JwtException(null, e));
        }
    }

    public Long extractMemberId(JwtToken token) {
        return Jwts.parserBuilder()
            .setSigningKey(JwtConstant.SECRET_KEY)
            .build()
            .parseClaimsJws(token.getToken())
            .getBody()
            .get(JwtConstant.CLAIM_NAME, Long.class);
    }
}
