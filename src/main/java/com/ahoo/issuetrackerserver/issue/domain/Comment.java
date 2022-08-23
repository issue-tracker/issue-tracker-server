package com.ahoo.issuetrackerserver.issue.domain;

import com.ahoo.issuetrackerserver.common.BaseEntity;
import com.ahoo.issuetrackerserver.common.exception.DuplicatedReactionException;
import com.ahoo.issuetrackerserver.common.exception.ErrorMessage;
import com.ahoo.issuetrackerserver.common.exception.UnAuthorizedException;
import com.ahoo.issuetrackerserver.member.domain.Member;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member author;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Reaction> reactions = new ArrayList<>();

    public static Comment of(Member author, String content, Issue issue) {
        return new Comment(null, author, content, issue, new ArrayList<>());
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void validateSameAuthor(Long memberId) {
        if (this.author.getId() != memberId) {
            throw new UnAuthorizedException(ErrorMessage.INVALID_AUTHOR);
        }
    }
}
