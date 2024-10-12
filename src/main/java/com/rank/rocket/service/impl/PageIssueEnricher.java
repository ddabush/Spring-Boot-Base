package com.rank.rocket.service.impl;

import com.rank.rocket.models.H1Issue;
import com.rank.rocket.models.H1Text;
import com.rank.rocket.models.PageIssue;
import com.rank.rocket.service.IPageIssueEnricher;

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
        return null;
    }

    @Override
    public PageIssue enrichH1Issue(H1Issue pageIssue) {
        for (H1Text h1 : pageIssue.getH1List()) {
            String h1String = h1.getText();
            h1ToPageIssues.putIfAbsent(h1String, new ArrayList<>());

            h1ToPageIssues.compute(h1String, (key, h1Issues) -> {
                if (h1Issues.size() > 0) {
                    h1.setDuplicate(true);
                    pageIssue.setValid(false);
                }
                for (H1Issue h1Issue : h1Issues) {
                    for (H1Text h1Texts : h1Issue.getH1List()) {
                        h1Texts.setDuplicate(true);
                        h1Issue.setValid(false);
                    }
                }
                h1Issues.add(pageIssue);
                return h1Issues;
            });

        }
        return pageIssue;
    }

}
