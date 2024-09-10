package org.sheep1500.toyadvertisementbackend.ads_join.exception;

public class JoinExistException extends RuntimeException {
    public JoinExistException() {
    }

    public JoinExistException(String message) {
        super(message);
    }
}
