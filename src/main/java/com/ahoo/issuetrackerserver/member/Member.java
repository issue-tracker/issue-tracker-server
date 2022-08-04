package com.ahoo.issuetrackerserver.member;

import com.ahoo.issuetrackerserver.auth.AuthProvider;
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
@EqualsAndHashCode(exclude = {"loginId", "password", "email", "nickname", "profileImage", "resourceOwnerId"}, callSuper = false)
public class Member extends BaseEntity {

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

    @Column(updatable = false)
    private String resourceOwnerId;

    public static Member of(Long id, String loginId, String password, String email, String nickname,
        String profileImage, AuthProvider authProviderType, String resourceOwnerId) {
        return new Member(id, loginId, password, email, nickname, profileImage, authProviderType, resourceOwnerId);
    }

    public static Member of(String loginId, String password, String email, String nickname,
        String profileImage, AuthProvider authProviderType, String resourceOwnerId) {
        return new Member(null, loginId, password, email, nickname, profileImage, authProviderType, resourceOwnerId);
    }

    public boolean isCorrectPassword(String password) {
        return this.password.toLowerCase().equals(password);
    }
}
