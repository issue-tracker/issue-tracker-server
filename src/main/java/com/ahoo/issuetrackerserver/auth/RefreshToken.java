package com.ahoo.issuetrackerserver.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(timeToLive = 60)
public class RefreshToken {

    @Id
    private String refreshToken;
}
