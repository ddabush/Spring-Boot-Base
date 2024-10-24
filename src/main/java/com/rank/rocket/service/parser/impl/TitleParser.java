package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.Severity;
import com.rank.rocket.models.TitleIssue;
import com.rank.rocket.service.parser.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class TitleParser implements IPageIssueParser<TitleIssue> {
    @Override
    public TitleIssue parsePage(Document doc) {
        String title = doc.title();
        int length = -1;
        IssueStatus status =IssueStatus.VALID;
        Severity severity = null;
        if (!StringUtils.isEmpty(title)) {
            length = title.length();
            status = calculateTitleStatus(length);
        }else{
            status = IssueStatus.MISSING;
            severity = Severity.HIGH;
        }


        if (status.equals(IssueStatus.LONG) || status.equals(IssueStatus.SHORT)){
           severity = Severity.MEDIUM;
        } else if (status.equals(IssueStatus.MISSING)) {
            severity = Severity.HIGH;
        }

        return TitleIssue.builder()
                .severity(severity)
                .status(status)
                .title(title)
                .length(length)
                .severity(severity)
                .build();

    }

    private IssueStatus calculateTitleStatus(int length) {
        if (length < 55)
            return IssueStatus.SHORT;
        if (length > 65)
            return IssueStatus.LONG;
        return IssueStatus.VALID;
    }
}
