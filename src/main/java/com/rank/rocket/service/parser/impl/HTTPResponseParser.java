package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.HTTPResponseIssue;
import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.Severity;
import org.jsoup.Connection;

public class HTTPResponseParser {

    public HTTPResponseIssue parse(int statusCode,String statusMessage) {
        String description = null;
        Severity severity = null;
        IssueStatus status = IssueStatus.VALID;
        if (statusCode >= 300 && statusCode < 400) {
            description = statusMessage;
            severity = Severity.MEDIUM;
            status =IssueStatus.BAD_HTTP_RESPONSE;
        } else if (statusCode >= 400) {
            description = statusMessage;
            severity = Severity.HIGH;
            status =IssueStatus.BAD_HTTP_RESPONSE;
        }
        return HTTPResponseIssue.builder()
                .code(statusCode)
                .description(description)
                .severity(severity)
                .status(status)
                .build();

    }
}
