package com.rank.rocket.service;

import com.rank.rocket.models.H1Issue;
import com.rank.rocket.models.PageIssue;

public interface IPageIssueEnricher{

    public PageIssue enrich(PageIssue pageIssue);
    public PageIssue enrichH1Issue(H1Issue pageIssue);
}
