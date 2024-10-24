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
public class H2Issue extends PageIssue {
    private List<String> h2List;
}
