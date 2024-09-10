package org.sheep1500.toyadvertisementbackend.lock;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class LockData {
    private String key;
    private TimeUnit timeUnit;
    private long waitTime;
    private long leaseTime;

    public LockData(String key) {
        this.key = key;
        this.timeUnit = TimeUnit.SECONDS;
        this.waitTime = 5L;
        this.leaseTime = 3L;
    }

    public LockData(String key, long waitTime, long leaseTime) {
        this(key);
        this.timeUnit = TimeUnit.SECONDS;
        this.waitTime = waitTime;
        this.leaseTime = leaseTime;

    }

    public LockData(String key, TimeUnit timeUnit, long waitTime, long leaseTime) {
        this(key, waitTime, leaseTime);
        this.timeUnit = timeUnit;
    }
}
