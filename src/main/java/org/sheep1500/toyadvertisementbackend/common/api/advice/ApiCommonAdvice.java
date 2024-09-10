package org.sheep1500.toyadvertisementbackend.common.api.advice;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsExistNameException;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.AdsJoinExistException;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.sheep1500.toyadvertisementbackend.common.api.response.ApiResponseDto;
import org.sheep1500.toyadvertisementbackend.common.api.response.RtCode;
import org.sheep1500.toyadvertisementbackend.lock.LockException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ApiCommonAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public ApiResponseDto<String> handleBaseException(Exception e) {
        log.error("[{},{}]", RtCode.RT_INTERNAL_ERROR.getRtCode(), e.getCause() != null ? e.getCause() : e);
        return ApiResponseDto.createException(RtCode.RT_INTERNAL_ERROR, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingPathVariableException.class)
    public ApiResponseDto<String> handleBaseException(MissingPathVariableException e) {
        ApiResponseDto<String> exception =
                ApiResponseDto.createException(RtCode.RT_MISSING_PATH_VARIABLE, e.getMessage());
        log.warn("[{}]", RtCode.RT_MISSING_PATH_VARIABLE.getRtCode(), e);
        return exception;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ApiResponseDto<String> handleBaseException(ConstraintViolationException e) {
        ApiResponseDto<String> exception =
                ApiResponseDto.createException(RtCode.RT_WRONG_PARAMETER, "Wrong parameters.");
        log.warn("[{}]", RtCode.RT_WRONG_PARAMETER.getRtCode(), e);
        return exception;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ConversionFailedException.class})
    public ApiResponseDto<String> handleValidException(ConversionFailedException e) {
        String errorMessage =
                String.format(
                        " %s Conversion failed for Object of a request.",
                        e.getTargetType().getType().getSimpleName());

        ApiResponseDto<String> exception =
                ApiResponseDto.createException(RtCode.RT_WRONG_PARAMETER, errorMessage);
        log.error("[{}]", RtCode.RT_WRONG_PARAMETER.getRtCode(), e);

        return exception;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ApiResponseDto<String> handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        String errorMessage =
                String.format(
                        " %s Validation failed for Object of a request.",
                        Objects.requireNonNull(result.getFieldError()).getField());

        ApiResponseDto<String> exception =
                ApiResponseDto.createException(RtCode.RT_VALIDATION_FAILURE, errorMessage);
        log.warn("[{}]", RtCode.RT_VALIDATION_FAILURE.getRtCode(), e);

        return exception;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BindException.class})
    public ApiResponseDto<ErrorMessageCollection> handleValidException(BindException e) {
        ApiResponseDto<ErrorMessageCollection> exception =
                ApiResponseDto.createException(
                        RtCode.RT_WRONG_PARAMETER,
                        new ErrorMessageCollection(
                                e.getBindingResult().getFieldErrors(), e.getBindingResult().getGlobalErrors()));
        log.warn("[{}]", RtCode.RT_BINDING_FAILURE.getRtCode(), e);
        return exception;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {LockException.class})
    public ApiResponseDto<String> handleLockException(LockException e) {
        log.error("[{},{}]", RtCode.RT_LOCK_FAIL.getRtCode(), e.getCause() != null ? e.getCause() : e);
        return ApiResponseDto.createException(RtCode.RT_LOCK_FAIL, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {AdsValidException.class, AdsExistNameException.class, DisableJoinAdsException.class, AdsJoinExistException.class, EntityNotFoundException.class})
    public ApiResponseDto<String> handleAdsException(Exception e) {
        log.error("[{},{}]", RtCode.RT_INTERNAL_ERROR.getRtCode(), e.getCause() != null ? e.getCause() : e);
        return ApiResponseDto.createException(RtCode.RT_INTERNAL_ERROR, e.getMessage());
    }
}
