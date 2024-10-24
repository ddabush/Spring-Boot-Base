package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.ImageAltTextIssue;
import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.Severity;
import com.rank.rocket.service.parser.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageAltTextParser implements IPageIssueParser<ImageAltTextIssue> {
    @Override
    public ImageAltTextIssue parsePage(Document doc) {
        Elements images = doc.select("img");
        List<String> missingAltTextImages = new ArrayList<>();
        IssueStatus status = IssueStatus.VALID;
        Severity severity = null;
        for (Element img : images) {
            String src = img.hasAttr("data-src") ? img.attr("data-src") : img.attr("src");
            String altText = img.attr("alt");
            if(StringUtils.isEmpty(altText)){
                severity = Severity.LOW;
                status = IssueStatus.MISSING;
                missingAltTextImages.add(src);
            }

        }

        return ImageAltTextIssue.builder().missingAltTextImages(missingAltTextImages).severity(severity).status(status).build();
    }
}
