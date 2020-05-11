package com.main.david.poller;

import com.main.david.poller.com.main.david.poller.worker.CleanUpWorker;
import com.main.david.poller.com.main.david.poller.worker.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PollerImpl implements IPoller {
    private ExecutorService pool ;
    private List<String> lineNumbers;
    private StopsCache stopsCache;
    private Queue<LinePoll> linePolls = new ConcurrentLinkedQueue<>();
    private  INextBusProvider provider;
    @Override
    public void init(PollerConfig config) {
        stopsCache=new StopsCache();
        this.lineNumbers = config.getLineNumbers();
        for (String line : lineNumbers) {
            LinePoll linePoll = new LinePoll(line, new Date());
            linePolls.add(linePoll);
        }
        int maxConcurrency=config.getMaxConcurrency();
        int pollIntervalSeconds = config.getPollIntervalSeconds();
        this.provider = config.getProvider();
        int numberOfThreads=Math.min(maxConcurrency,lineNumbers.size());
        pool = Executors.newFixedThreadPool(numberOfThreads);


        for(int i=0;i<numberOfThreads;i++){
            Worker worker=new Worker(linePolls,pollIntervalSeconds,provider,stopsCache);
            pool.submit(worker);
        }
        CleanUpWorker cleanupWorker= new CleanUpWorker(stopsCache);
        Thread CleanUpWorkerThread=new Thread(cleanupWorker);
        CleanUpWorkerThread.start();



    }
    public List<Row> printCSV(int iterations,String line){
        long currentTime = System.currentTimeMillis();
        int tripId=1;
        List<Row> result=new ArrayList();
        List<StopInterval> lineIntervals = provider.getLineIntervals(line);
        Map<Integer,StopInterval> stopToInterval=new HashMap();
        Map<Integer,StopEta> trips=new HashMap();
        for(StopInterval interval : lineIntervals ){
            stopToInterval.put(interval.toStopId,interval);
        }
        List<StopEta> lineEtas = provider.getLineEta(line);
        StopEta lastStop=null;
        for(StopEta lineEta:lineEtas){
            if(lastStop==null){
                Row row = new Row();
                row.setEta(lineEta.getEta());
                row.setLinNumber(line);
                row.setNextStopId(lineEta.getStopId());
                row.setTimeStamp(currentTime);
                row.setTripId(tripId);
                trips.put(tripId,lineEta);
                tripId++;
            }else{
                if(lastStop.getEta().getTime()>lineEta.getEta().getTime()
                        || lastStop.getEta().getTime()+stopToInterval.get(lineEta.getStopId()).intervalSeconds > lineEta.getEta().getTime()){
                    Row row = new Row();
                    row.setEta(lineEta.getEta());
                    row.setLinNumber(line);
                    row.setNextStopId(lineEta.getStopId());
                    row.setTimeStamp(currentTime);
                    row.setTripId(tripId);
                    tripId++;
                    trips.put(tripId,lineEta);
                    result.add(row);

                }
                lastStop=lineEta;
            }




        }


        for(int i=1;i<iterations;i++) {
            currentTime = System.currentTimeMillis();
            lineEtas = provider.getLineEta(line);
            for (StopEta lineEta : lineEtas) {
                if (lastStop.getEta().getTime() > lineEta.getEta().getTime()
                        || lastStop.getEta().getTime() + stopToInterval.get(lineEta.getStopId()).intervalSeconds < lineEta.getEta().getTime()) {
                    Row row = new Row();
                    row.setEta(lineEta.getEta());
                    row.setLinNumber(line);
                    row.setNextStopId(lineEta.getStopId());
                    row.setTimeStamp(currentTime);
                    row.setTripId(tripId);
                    trips.put(tripId,lineEta);
                    tripId++;
                    result.add(row);

                }

            }
        }
        return result;

    }

    @Override
    public List<LineEta> getStopArrivals(int stopId) {
        List<LineEta> stopArrivals=new ArrayList<>();
        stopArrivals.addAll(stopsCache.getArrivals(stopId));
        return stopArrivals;

    }
}
