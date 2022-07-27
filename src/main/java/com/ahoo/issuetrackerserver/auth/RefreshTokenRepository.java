package com.ahoo.issuetrackerserver.auth;

import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
