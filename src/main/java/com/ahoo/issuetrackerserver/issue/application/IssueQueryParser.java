package com.ahoo.issuetrackerserver.issue.application;

import com.ahoo.issuetrackerserver.issue.presentation.dto.IssueSearchFilter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IssueQueryParser {

    public static IssueSearchFilter parse(String q) {
        IssueSearchFilter filter = new IssueSearchFilter();

        if (q == null) {
            return filter;
        }

        String[] queryStrings = q.split("\\+");
        for (String queryString : queryStrings) {
            String decodedQueryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
            filter.addElement(decodedQueryString);
        }
        return filter;
    }

}
