package com.rank.rocket.service;

import com.rank.rocket.models.PageIssue;
import org.jsoup.nodes.Document;

public interface IPageIssueParser<T extends PageIssue> {
    public T parsePage(Document doc);
}
