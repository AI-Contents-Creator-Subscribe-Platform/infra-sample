package org.sheep1500.toyadvertisementbackend.lock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LockRepository extends JpaRepository<LockData, LockPK> {
    List<LockData> findAllByKey(String key);
    Optional<LockData> findByLockId(LockId lockId);
}
