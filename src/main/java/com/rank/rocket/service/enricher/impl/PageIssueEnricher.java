package com.rank.rocket.service.enricher.impl;

import com.rank.rocket.models.*;
import com.rank.rocket.service.enricher.IPageIssueEnricher;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class PageIssueEnricher implements IPageIssueEnricher {
    ConcurrentHashMap<String, List<H1Issue>> h1ToPageIssues = new ConcurrentHashMap<>();

    @Override
    public PageIssue enrich(PageIssue pageIssue) {
        if(pageIssue instanceof H1Issue){
            return enrichH1Issue((H1Issue) pageIssue);
        }
        return pageIssue;
    }

    private PageIssue enrichH1Issue(H1Issue pageIssue) {
        for (H1Text h1 : pageIssue.getH1List()) {
            String h1String = h1.getText();
            if(StringUtils.isEmpty(h1String)){
                continue;
            }
            h1ToPageIssues.putIfAbsent(h1String, new ArrayList<>());

            h1ToPageIssues.compute(h1String, (key, h1Issues) -> {
                if (h1Issues.size() > 0) {
                    h1.setDuplicate(true);
                    pageIssue.setSeverity(Severity.HIGH);
                    pageIssue.setStatus(IssueStatus.DUPLICATE);
                }
                for (H1Issue h1Issue : h1Issues) {
                    for (H1Text h1Text : h1Issue.getH1List()) {
                        if(h1Text.getText().equals(h1String)){
                            h1Text.setDuplicate(true);
                        }
                    }
                        h1Issue.setSeverity(Severity.HIGH);
                        h1Issue.setStatus(IssueStatus.DUPLICATE);
                }
                h1Issues.add(pageIssue);
                return h1Issues;
            });

        }
        return pageIssue;
    }

}
