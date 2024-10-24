package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.H1Issue;
import com.rank.rocket.models.H1Text;
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
public class H1Parser implements IPageIssueParser<H1Issue> {


    @Override
    public H1Issue parsePage(Document doc) {
        Elements h1Tags = doc.select("h1");
        List<H1Text> h1List = new ArrayList<>();
        Severity severity= null;
        IssueStatus h1Status = IssueStatus.VALID;
        if (!h1Tags.isEmpty()) {
            for (Element h1Tag : h1Tags) {
                String h1 = h1Tag.text();
                int length = h1.length();
                if(length==0){
                    severity=Severity.HIGH;
                    h1Status = IssueStatus.MISSING;
                }
                if(length<55){
                    severity=Severity.LOW;
                    h1Status = IssueStatus.SHORT;
                }
                if(length>65){
                    severity=Severity.LOW;
                    h1Status = IssueStatus.LONG;
                }
                h1List.add(new H1Text(h1, false, length));
            }
        }else{
            severity = Severity.HIGH;
            h1Status= IssueStatus.MISSING;
        }
        return H1Issue.builder()
                .status(h1Status)
                .severity(severity)
                .h1List(h1List)
                .build();
    }
}
