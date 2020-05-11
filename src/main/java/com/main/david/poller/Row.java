package com.main.david.poller;

import lombok.Setter;

import java.util.Date;

@Setter
public class Row {
    long timeStamp;
    String linNumber;
    Integer nextStopId;
    Date eta;
    int tripId;
}
