package org.sheep1500.toyadvertisementbackend.lock;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

@Component
public class DbLockManager implements LockManager {

    private static final long EXPIRE_TIMEOUT_MS = 10 * 60 * 1000;
    private final LockRepository lockRepository;

    public DbLockManager(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public LockId tryLock(String type, String key) throws LockException {
        checkAlreadyLocked(key);
        LockId lockId = new LockId(UUID.randomUUID().toString());
        locking(type, key, lockId, EXPIRE_TIMEOUT_MS);
        return lockId;
    }

    private void checkAlreadyLocked(String key) {
        List<LockData> locks = lockRepository.findAllByKey(key);
        Optional<LockData> lockData = handleExpiration(locks);
        if (lockData.isPresent()) throw new AlreadyLockedException();
    }

    private Optional<LockData> handleExpiration(List<LockData> locks) {
        if (locks.isEmpty()) return Optional.empty();
        LockData lockData = locks.get(0);
        if (lockData.isExpired()) {
            lockRepository.delete(lockData);
            lockRepository.flush();
            return Optional.empty();
        } else {
            return Optional.of(lockData);
        }
    }

    private void locking(String type, String key, LockId lockId, long timeoutMs) {
        try {
            LockData lockData = new LockData(type, key, lockId, timeoutMs);
            lockRepository.saveAndFlush(lockData);
        } catch (DuplicateKeyException e) {
            throw new LockingFailException(e);
        } catch (DataIntegrityViolationException e) {
            throw new LockingFailException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void releaseLock(String type, String key) throws LockException {
        lockRepository.deleteById(new LockPK(type, key));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void releaseLock(LockId lockId) throws LockException {
        Optional<LockData> lockData = lockRepository.findByLockId(lockId);
        if(lockData.isEmpty()) {
            throw new LockNotExistException();
        }
        lockRepository.delete(lockData.get());
    }
}