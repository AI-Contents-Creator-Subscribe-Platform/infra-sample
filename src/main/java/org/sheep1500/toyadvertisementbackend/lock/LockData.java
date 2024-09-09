package org.sheep1500.toyadvertisementbackend.lock;


import jakarta.persistence.*;
import org.sheep1500.payment.common.jpa.LockIdConverter;

@Entity
@Table(name = "locks")
@IdClass(LockPK.class)
public class LockData {
    @Id
    @Column(name = "lock_type", nullable = false)
    private String type;

    @Id
    @Column(name = "lock_key", nullable = false)
    private String key;

    @Convert(converter = LockIdConverter.class)
    @Column(name = "lock_id", nullable = false)
    private LockId lockId;

    @Column(nullable = false)
    private long lockTimestamp;

    @Column
    private long expirationTimestamp;

    protected LockData() {}

    public LockData(String type, String key, LockId lockId, long timeoutMs) {
        this.type = type;
        this.key = key;
        this.lockId = lockId;
        this.lockTimestamp = System.currentTimeMillis();
        this.expirationTimestamp = this.lockTimestamp + timeoutMs;
    }

    public boolean isExpired() {
        return expirationTimestamp < System.currentTimeMillis();
    }
}