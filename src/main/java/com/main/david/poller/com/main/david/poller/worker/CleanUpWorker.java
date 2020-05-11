package com.main.david.poller.com.main.david.poller.worker;

import com.main.david.poller.StopsCache;

public class CleanUpWorker implements Runnable {
    private StopsCache stopsCache;
    public CleanUpWorker(StopsCache stopsCache){
        this.stopsCache=stopsCache;
    }
    @Override
    public void run() {
        while(true){
            try {
                wait(10000);
                stopsCache.emptyOldRecords();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
