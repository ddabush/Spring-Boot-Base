package com.rank.rocket.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class WebPageMetaData {
    private boolean crawlingFailed;
    private String failedToCrawlReason;
    private String url;
    private int httpStatus;
    private boolean hasH1;
    private List<String> h1;

}
