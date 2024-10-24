package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.H2Issue;
import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.Severity;
import com.rank.rocket.service.parser.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class H2Parser implements IPageIssueParser<H2Issue> {

    @Override
    public H2Issue parsePage(Document doc) {
        Elements h2Tags = doc.select("h2");
        List<String> h2List = new ArrayList<>();
        Severity severity = null;
        IssueStatus status = IssueStatus.VALID;
        if (!h2Tags.isEmpty()) {
            for (Element h2Tag : h2Tags) {
                String h2 = h2Tag.text();
                h2List.add(h2);
            }
        }else{
            status = IssueStatus.MISSING;
            severity = Severity.LOW;
        }

        return H2Issue.builder()
                .severity(severity)
                .status(status)
                .h2List(h2List)
                .build();
    }
}
