package com.main.david.poller.com.main.david.poller.worker;

import com.main.david.poller.*;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


@Getter
@Setter
public class Worker implements Runnable {
    private INextBusProvider provider;
    private StopsCache stopsCache;
    private Queue<LinePoll> linePolls;
    private long pollIntervalInMillis;

    public Worker(Queue<LinePoll> linePolls, int pollIntervalSeconds, INextBusProvider provider, StopsCache stopsCache) {
        this.provider = provider;
        this.stopsCache = stopsCache;
        this.linePolls = linePolls;
        long pollIntervalInMillis = pollIntervalSeconds * 1000;
    }

    @Override
    public void run() {
        while (true) {
            LinePoll linePoll = linePolls.poll();
            if (canCallProvider(linePoll.getLastPoll().getTime())) {
                List<StopEta> stopEtas = provider.getLineEta(linePoll.getLineNumber());
                for (StopEta stopEta : stopEtas) {
                    LineEta lineEta = new LineEta(linePoll.getLineNumber(), stopEta.getEta());
                    stopsCache.addArrival(stopEta.getStopId(), lineEta);
                }
                linePoll.setLastPoll(new Date());
            }
            linePolls.add(linePoll);


        }
    }


    public boolean canCallProvider(long lastPollTime) {
        long currentTime = System.currentTimeMillis();
        long maxTimeAllowed = currentTime - this.pollIntervalInMillis;
        return lastPollTime < maxTimeAllowed;
    }
}
