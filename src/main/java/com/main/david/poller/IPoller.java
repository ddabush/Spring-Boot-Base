package com.main.david.poller;

import java.util.List;

public interface IPoller {
    void init(PollerConfig config);
    List<LineEta> getStopArrivals(int stopId);

}
