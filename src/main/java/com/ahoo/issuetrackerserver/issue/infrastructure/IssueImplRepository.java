package com.ahoo.issuetrackerserver.issue.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IssueImplRepository implements IssueCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

}
