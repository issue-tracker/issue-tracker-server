package com.ahoo.issuetrackerserver.auth.infrastructure;

import com.ahoo.issuetrackerserver.auth.infrastructure.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
