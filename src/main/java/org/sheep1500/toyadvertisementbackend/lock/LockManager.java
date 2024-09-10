package org.sheep1500.toyadvertisementbackend.lock;

public interface LockManager {
    boolean tryLock(LockData lockData) throws LockException;
    void releaseLock(String key) throws LockException;
}
