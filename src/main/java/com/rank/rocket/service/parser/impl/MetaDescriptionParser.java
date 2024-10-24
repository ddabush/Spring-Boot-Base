package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.MetaDescriptionIssue;
import com.rank.rocket.models.Severity;
import com.rank.rocket.service.parser.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MetaDescriptionParser implements IPageIssueParser<MetaDescriptionIssue> {
    @Override
    public MetaDescriptionIssue parsePage(Document doc) {
        int length = -1;
        IssueStatus status = IssueStatus.VALID;
        Severity severity = null;
        String description = null;
        Element metaDescription = doc.selectFirst("meta[name=description]");
        if (metaDescription != null) {
            description = metaDescription.attr("content");
        }


        if (!StringUtils.isEmpty(description)) {
            length = description.length();
            status = calculateTitleStatus(length);
        } else {
            status = IssueStatus.MISSING;
            severity = Severity.HIGH;
        }


        if (status.equals(IssueStatus.LONG) || status.equals(IssueStatus.SHORT)) {
            severity = Severity.LOW;
        } else if (status.equals(IssueStatus.MISSING)) {
            severity = Severity.HIGH;
        }

        return MetaDescriptionIssue.builder()
                .severity(severity)
                .status(status)
                .description(description)
                .length(length)
                .severity(severity)
                .build();
    }

    private IssueStatus calculateTitleStatus(int length) {
        if (length < 150)
            return IssueStatus.SHORT;
        if (length > 170)
            return IssueStatus.LONG;
        return IssueStatus.VALID;
    }
}
