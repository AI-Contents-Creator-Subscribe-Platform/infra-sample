package org.sheep1500.toyadvertisementbackend.facade;

import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.sheep1500.toyadvertisementbackend.lock.LockException;
import org.sheep1500.toyadvertisementbackend.lock.LockManager;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class LockAdsFacade {
    private final LockManager lockManager;

    public <T> T locking(LockData lockData, Supplier<T> supplier) {
        try {
            if (!lockManager.tryLock(lockData)) {
                throw new RuntimeException();
            }

            return supplier.get();
        } catch (LockException e) {
            throw new RuntimeException();
        } finally {
            lockManager.releaseLock(lockData.getKey());
        }
    }
}
