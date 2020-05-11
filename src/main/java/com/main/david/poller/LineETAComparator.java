package com.main.david.poller;

import java.util.Comparator;

public class LineETAComparator implements Comparator <LineEta>{
    @Override
    public int compare(LineEta o1, LineEta o2) {
        return o1.getEta().compareTo(o2.getEta());
    }
}
