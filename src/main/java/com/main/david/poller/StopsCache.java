package com.main.david.poller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


public class StopsCache {
    private Map<Integer, Set<LineEta>> stopArrivals = new HashMap<>();

    public void addArrival(Integer stopId, LineEta arrival) {
        Set<LineEta> stopData = stopArrivals.get(stopId);
        if (stopData == null) {
            synchronized (this) {
                if (stopData == null) {
                    stopData = new ConcurrentSkipListSet<LineEta>();
                }
            }
        }
        stopData.add(arrival);
    }

    public Set<LineEta> getArrivals(Integer stopId) {
        Set<LineEta> stopData = stopArrivals.get(stopId);
        return stopData;

    }
    public void emptyOldRecords(){
        for(Set<LineEta> arrivals :stopArrivals.values()){
            for(LineEta arrival: arrivals){
                long currentTime=System.currentTimeMillis();
                if(arrival.getEta().getTime() < currentTime){
                    arrivals.remove(arrival);
                }
            }
        }

    }
}
