package com.rank.rocket.models;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@SuperBuilder
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HTTPResponseIssue.class, name = "httpResponse"),
        @JsonSubTypes.Type(value = H1Issue.class, name = "h1"),
        @JsonSubTypes.Type(value = H2Issue.class, name = "h2"),
        @JsonSubTypes.Type(value = TitleIssue.class, name = "title"),
        @JsonSubTypes.Type(value = MetaDescriptionIssue.class, name = "metaDescription"),
        @JsonSubTypes.Type(value = ImageAltTextIssue.class, name = "imageAltText"),
        @JsonSubTypes.Type(value = CanonicalIssue.class, name = "canonical")
})
public class PageIssue {
    private Severity severity;
    private IssueStatus status;
}
