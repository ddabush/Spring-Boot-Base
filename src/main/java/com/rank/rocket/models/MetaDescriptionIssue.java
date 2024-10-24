package com.rank.rocket.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@Getter
@Setter
public class MetaDescriptionIssue extends PageIssue{
    private String description;
    private int length;
}
