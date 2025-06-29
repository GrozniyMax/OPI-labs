package com.web.primefacesexampletest.mbeans;

public interface DotCounterMBean {

    long getTotal();

    long getHits();

    void checkForConsecutiveMisses();
}
