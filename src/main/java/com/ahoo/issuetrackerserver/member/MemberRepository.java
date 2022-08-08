package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.auth.AuthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsBySignInId(String signInId);

    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByAuthProviderTypeAndResourceOwnerId(AuthProvider authProviderType, String resourceOwnerId);

    Optional<Member> findBySignInId(String signInId);
}
