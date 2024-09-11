package org.sheep1500.toyadvertisementbackend.common.api.response;

import org.springframework.http.HttpStatus;

public enum RtCode {

    RT_SUCCESS(0, "Success", HttpStatus.OK),
    RT_INTERNAL_ERROR(-1, "Internal error", HttpStatus.INTERNAL_SERVER_ERROR),
    RT_WRONG_PARAMETER(-2, "Wrong parameter", HttpStatus.BAD_REQUEST),
    RT_MISSING_PATH_VARIABLE(-3, "Missing path variable", HttpStatus.BAD_REQUEST),
    RT_VALIDATION_FAILURE(-4, "Validation error", HttpStatus.BAD_REQUEST),
    RT_BINDING_FAILURE(-5, "Binding error", HttpStatus.BAD_REQUEST),
    RT_AUTHENTICATION_FAILURE(-6, "Authentication failure", HttpStatus.FORBIDDEN),
    RT_LOCK_FAIL(-7, "Lock fail", HttpStatus.INTERNAL_SERVER_ERROR);

    private final long rtCode;
    private final String rtMessage;
    private final HttpStatus httpStatus;

    private RtCode(long rtCode, String rtMessage, HttpStatus httpStatus) {
        this.rtCode = rtCode;
        this.rtMessage = rtMessage;
        this.httpStatus = httpStatus;
    }

    public long getRtCode() {
        return this.rtCode;
    }

    public String getRtMessage() {
        return this.rtMessage;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(this.rtMessage).append("] ");

        return sb.toString();
    }
}
