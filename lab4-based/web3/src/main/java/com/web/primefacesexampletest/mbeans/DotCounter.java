package com.web.primefacesexampletest.mbeans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import javax.management.*;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Named("dotCounter")
@SessionScoped
public class DotCounter implements DotCounterMBean, NotificationBroadcaster, Serializable {

    private final AtomicLong total = new AtomicLong();

    private final AtomicLong hits = new AtomicLong();

    private final AtomicInteger oneByOneMissesNumber = new AtomicInteger();

    private final NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();

    public long getTotal() {
        return total.get();
    }

    public long getHits() {
        return hits.get();
    }

    @Override
    public void checkForConsecutiveMisses() {
        if (oneByOneMissesNumber.get() >= 2) {
            broadcaster.sendNotification(new Notification(
                    "consecutive.misses",
                    this,
                    System.currentTimeMillis(),
                    "2 consecutive misses detected."
            ));

            oneByOneMissesNumber.set(0);
        }
    }

    public void updateAttempt(boolean hit) {
        total.incrementAndGet();
        if (!hit) {

            oneByOneMissesNumber.incrementAndGet();
        } else {
            hits.incrementAndGet();
            oneByOneMissesNumber.set(0);
        }
        checkForConsecutiveMisses();
    }

    @Override
    public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback) throws IllegalArgumentException {
        broadcaster.addNotificationListener(listener, filter, handback);
    }

    @Override
    public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException {
        broadcaster.removeNotificationListener(listener);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[]{"oneByOne.misses"};
        String name = NotificationBroadcasterSupport.class.getName();
        String description = "Notification sent when 2 misses one by one are recorded";
        return new MBeanNotificationInfo[] { new MBeanNotificationInfo(types, name, description) };
    }
}
