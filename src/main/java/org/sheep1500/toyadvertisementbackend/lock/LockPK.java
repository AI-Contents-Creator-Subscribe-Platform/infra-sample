package org.sheep1500.toyadvertisementbackend.lock;

import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;


@Getter
public class LockPK implements Serializable {
    private String type;
    private String key;

    protected LockPK() {}

    public LockPK(String type, String key) {
        this.type = type;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        LockPK lockId = (LockPK) o;
        return Objects.equals(key, lockId.getKey()) && Objects.equals(type, lockId.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, key);
    }
}
