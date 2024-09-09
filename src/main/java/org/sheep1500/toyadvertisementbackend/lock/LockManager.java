package org.sheep1500.toyadvertisementbackend.lock;

public interface LockManager {
    LockId tryLock(String type, String key) throws LockException;

    void releaseLock(String type, String key) throws LockException;
    void releaseLock(LockId lockId) throws LockException;
}
