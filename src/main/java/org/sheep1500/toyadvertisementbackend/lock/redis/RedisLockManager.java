package org.sheep1500.toyadvertisementbackend.lock.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sheep1500.toyadvertisementbackend.lock.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockManager implements LockManager {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryLock(LockData lockData) throws LockException {
        RLock rLock = redissonClient.getLock(lockData.getKey());
        try {
            return rLock.tryLock(lockData.getWaitTime(), lockData.getLeaseTime(), lockData.getTimeUnit());
        } catch (InterruptedException e) {
            throw new LockingFailException(e);
        }
    }

    @Override
    public void releaseLock(String key) throws LockException {
        RLock rLock = redissonClient.getLock(key);
        if (rLock == null || !rLock.isLocked()) {
            throw new LockNotExistException();
        }
        rLock.unlock();
    }
}
