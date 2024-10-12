package com.rank.rocket.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class H1Text {
    String text;
    boolean isDuplicate;
    private int length;
}
