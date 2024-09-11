package org.sheep1500.toyadvertisementbackend.facade;

import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.sheep1500.toyadvertisementbackend.lock.LockException;
import org.sheep1500.toyadvertisementbackend.lock.LockManager;
import org.sheep1500.toyadvertisementbackend.lock.LockingFailException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class LockAdsFacade {
    private final LockManager lockManager;

    public <T> T executeWithLock(LockData lockData, Supplier<T> supplier) {
        try {
            if (!lockManager.tryLock(lockData)) {
                throw new LockingFailException();
            }

            return supplier.get();
        } catch (LockException e) {
            throw new LockException(e);
        } finally {
            lockManager.releaseLock(lockData.getKey());
        }
    }

    public void executeWithLock(LockData lockData, Runnable task) {
        try {
            if (!lockManager.tryLock(lockData)) {
                throw new LockingFailException();
            }
            task.run();
        } catch (LockException e) {
            throw new LockException(e);
        } finally {
            lockManager.releaseLock(lockData.getKey());
        }
    }
}
