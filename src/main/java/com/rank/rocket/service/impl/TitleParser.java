package com.rank.rocket.service.impl;

import com.rank.rocket.models.TitleIssue;
import com.rank.rocket.service.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class TitleParser implements IPageIssueParser<TitleIssue> {
    @Override
    public TitleIssue parsePage(Document doc) {
        Elements titleElement = doc.select("title");
        String title = null;
        int length = -1;
        TitleIssue.TitleStatus titleStatus = TitleIssue.TitleStatus.MISSING;
        if (!titleElement.isEmpty()) {
            title = titleElement.text();
            if (title != null) {
                length = title.length();
                titleStatus = calculateTitleStatus(length);
            }
        }
        boolean isValid = titleStatus.equals(TitleIssue.TitleStatus.VALID);
        return TitleIssue.builder()
                .valid(isValid)
                .titleStatus(titleStatus)
                .title(title)
                .length(length)
                .build();

    }

    private TitleIssue.TitleStatus calculateTitleStatus(int length) {
        if (length < 50)
            return TitleIssue.TitleStatus.SHORT;
        if (length > 60)
            return TitleIssue.TitleStatus.LONG;
        return TitleIssue.TitleStatus.VALID;
    }
}
