package com.rank.rocket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class WebPageMetaData {
    private String url;
    private boolean isPageValid;
    private List<PageIssue> issues;

}
