package com.rank.rocket.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@Getter
@Setter
public class H1Issue extends PageIssue {
    private boolean hasH1;
    private List<H1Text> h1List;
}
