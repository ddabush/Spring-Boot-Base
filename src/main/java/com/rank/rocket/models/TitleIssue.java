package com.rank.rocket.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@ToString
@SuperBuilder
@Getter
public class TitleIssue extends PageIssue {
    private String title;
    private int length;
    private TitleStatus titleStatus;


    public enum TitleStatus {
        VALID,
        MISSING,
        LONG,
        SHORT
    }
}
