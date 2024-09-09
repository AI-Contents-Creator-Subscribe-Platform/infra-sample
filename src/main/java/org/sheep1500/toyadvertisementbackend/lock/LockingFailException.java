package org.sheep1500.toyadvertisementbackend.lock;

public class LockingFailException extends LockException {
    public LockingFailException() {
    }

    public LockingFailException(Exception cause) {
        super(cause);
    }
}
