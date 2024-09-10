package org.sheep1500.toyadvertisementbackend.ads_join.exception;

public class AdsJoinExistException extends RuntimeException {
    private static final String MESSAGE = "Ads join already exists";
    public AdsJoinExistException() {
        this(MESSAGE);
    }

    public AdsJoinExistException(String message) {
        super(message);
    }
}
