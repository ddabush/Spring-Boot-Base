package com.rank.rocket.crawler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SiteRules {
    private List<String> allowedPatterns;
    private List<String> disAllowedPatterns;

}
