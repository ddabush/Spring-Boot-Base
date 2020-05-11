package com.main.david.poller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PollerConfig {
    private INextBusProvider provider;
    private List<String> lineNumbers;
    private int pollIntervalSeconds;
    private int maxConcurrency;

}
