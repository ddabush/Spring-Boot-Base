package com.rank.rocket.service.parser.impl;

import com.rank.rocket.models.CanonicalIssue;
import com.rank.rocket.models.IssueStatus;
import com.rank.rocket.models.Severity;
import com.rank.rocket.service.parser.IPageIssueParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Component
public class CanonicalParser implements IPageIssueParser<CanonicalIssue> {
    @Override
    public CanonicalIssue parsePage(Document doc) {
        String canonicalURL=null;
        IssueStatus status = IssueStatus.VALID;
        Severity severity = null;
        boolean isSelfCanonical=false;
        boolean isCanonicalToDifferentURL=false;
        try {
            // Extract the canonical link if it exists
            Element canonicalElement = doc.selectFirst("link[rel=canonical]");
            String url = doc.baseUri();
            if (canonicalElement == null || StringUtils.isEmpty(canonicalElement.attr("href"))) {
                status = IssueStatus.MISSING;
                severity = Severity.HIGH;
            } else {
                String canonicalUrl = canonicalElement.attr("href");
                URI currentUri = new URI(url);
                URI canonicalUri = new URI(canonicalUrl);
                canonicalURL = canonicalUri.toURL().toString();
                // Check if the canonical URL points to a different page
                if (canonicalUri.equals(currentUri)) {
                    isSelfCanonical = true;
                } else {
                    isCanonicalToDifferentURL = true;
                }
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error fetching or parsing the page.");
        }
        return CanonicalIssue.builder()
                .canonicalURL(canonicalURL)
                .severity(severity)
                .status(status)
                .isSelfCanonical(isSelfCanonical)
                .isCanonicalToDifferentURL(isCanonicalToDifferentURL)
                .build();

    }
}
