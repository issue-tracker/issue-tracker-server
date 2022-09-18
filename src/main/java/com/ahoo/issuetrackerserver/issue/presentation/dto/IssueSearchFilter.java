package com.ahoo.issuetrackerserver.issue.presentation.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueSearchFilter {

    private static int KEY = 0;
    private static int VALUE = 1;

    private Boolean isClosed;
    private List<String> assigneeNicknames = new ArrayList<>();
    private List<String> labelTitles = new ArrayList<>();
    private String milestoneTitle;
    private String authorNickname;
    private String issueTitle;

    public void addElement(String queryString) {
        String[] query = queryString.split(":");
        if (query.length > 1) {
            query[VALUE] = query[VALUE].substring(1, query[VALUE].length() - 1);
        }
        switch (query[KEY]) {
            case "is":
                if (query[VALUE].equals("open")) {
                    isClosed = false;
                } else if (query[VALUE].equals("closed")) {
                    isClosed = true;
                } else {
                    throw new IllegalArgumentException("잘못된 상태(open, closed) 입력입니다.");
                }
                break;
            case "assignee":
                assigneeNicknames.add(query[VALUE]);
                break;
            case "label":
                labelTitles.add(query[VALUE]);
                break;
            case "milestone":
                milestoneTitle = query[VALUE];
                break;
            case "author":
                authorNickname = query[VALUE];
                break;
            default: // key:value로 이루어지지 않은 검색어는 이슈 타이틀
                issueTitle = query[KEY];
        }
    }
}
