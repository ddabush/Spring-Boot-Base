package com.rank.rocket.service.impl;

import com.rank.rocket.models.H1Issue;
import com.rank.rocket.models.H1Text;
import com.rank.rocket.service.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class H1Parser implements IPageIssueParser<H1Issue> {

    @Override
    public H1Issue parsePage(Document doc) {
        Elements h1Tags = doc.select("h1");
        List<H1Text> h1List = new ArrayList<>();

        boolean hasH1 = false;
        if (!h1Tags.isEmpty()) {
            hasH1 = true;
            for (Element h1Tag : h1Tags) {
                String h1 = h1Tag.text();
                int length = h1.length();
                h1List.add(new H1Text(h1, false, length));
            }
        }
        boolean isValid = hasH1;
        return H1Issue.builder()
                .valid(isValid)
                .hasH1(hasH1)
                .h1List(h1List)
                .build();
    }
}
