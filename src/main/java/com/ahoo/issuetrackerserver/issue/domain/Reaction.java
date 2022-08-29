package com.ahoo.issuetrackerserver.issue.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import com.ahoo.issuetrackerserver.common.exception.UnAuthorizedException;
import com.ahoo.issuetrackerserver.member.domain.Member;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reactor_id")
    private Member reactor;

    public static Reaction of(Comment comment, Emoji emoji, Member reactor) {
        return new Reaction(null, comment, emoji, reactor);
    }

    public boolean isSameReaction(Emoji emoji, Member reactor) {
        return getEmoji().equals(emoji) && getReactor().equals(reactor);
    }

    public void validateReactor(Long memberId) {
        if (!Objects.equals(getReactor().getId(), memberId)) {
            throw new UnAuthorizedException(ErrorType.INVALID_AUTHOR);
        }
    }
}
