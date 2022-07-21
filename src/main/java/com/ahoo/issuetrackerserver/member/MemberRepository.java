package com.ahoo.issuetrackerserver.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
    Optional<Member> findByEmail(String email);
}
