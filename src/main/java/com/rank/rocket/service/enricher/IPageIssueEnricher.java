package com.rank.rocket.service.enricher;

import com.rank.rocket.models.H1Issue;
import com.rank.rocket.models.PageIssue;

import java.util.concurrent.ExecutionException;

public interface IPageIssueEnricher{

    public PageIssue enrich(PageIssue pageIssue);
}
