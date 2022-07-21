package com.ahoo.issuetrackerserver.member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"loginId", "password", "email", "nickname", "profileImage"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 12)
    private String loginId;

    @Column(length = 16)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 12)
    private String nickname;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProviderType;

	public static Member of(Long id, String loginId, String password, String email, String nickname,
		String profileImage, AuthProvider authProviderType) {
		return new Member(id, loginId, password, email, nickname, profileImage, authProviderType);
	}

	public static Member of(String loginId, String password, String email, String nickname,
		String profileImage, AuthProvider authProviderType) {
		return new Member(null, loginId, password, email, nickname, profileImage, authProviderType);
	}
}
