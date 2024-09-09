package org.sheep1500.toyadvertisementbackend.lock;

public class LockException extends RuntimeException {
    public LockException() {
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(Throwable cause) {
        super(cause);
    }
}
