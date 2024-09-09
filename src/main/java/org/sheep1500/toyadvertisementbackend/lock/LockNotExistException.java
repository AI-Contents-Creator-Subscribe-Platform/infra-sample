package org.sheep1500.toyadvertisementbackend.lock;

public class LockNotExistException extends LockException {
    public LockNotExistException() {
    }

    public LockNotExistException(Exception cause) {
        super(cause);
    }
}
