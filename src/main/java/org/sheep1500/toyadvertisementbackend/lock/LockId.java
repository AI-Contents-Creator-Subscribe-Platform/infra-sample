package org.sheep1500.toyadvertisementbackend.lock;


import java.util.Objects;

public class LockId {
    private String value;

    public LockId(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockId lockId = (LockId) o;
        return Objects.equals(value, lockId.value);
    }
}
