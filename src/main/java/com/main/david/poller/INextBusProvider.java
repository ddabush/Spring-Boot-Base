package com.main.david.poller;


import java.util.List;

public interface INextBusProvider {
    List<StopEta> getLineEta(String lineNumber);
    List<StopInterval> getLineIntervals(String lineNumber);
}
