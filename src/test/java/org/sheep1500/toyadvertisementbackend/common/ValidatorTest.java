package org.sheep1500.toyadvertisementbackend.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.ads.presentation.dto.AdsRequest;
import org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto.AdsJoinRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValidatorTest {
    @Autowired
    private Validator validator;

    @DisplayName("광고 등록 request 유효성 검사 성공")
    @Test
    void violation_adsRequest_create_success() {
        // given
        AdsRequest.Create request = new AdsRequest.Create("name", BigDecimal.ZERO, 0, "text", "imageUrl", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));

        // when
        Set<ConstraintViolation<AdsRequest.Create>> violations = validator.validate(request);

        // then
        assertEquals(0, violations.size());
    }

    @DisplayName("광고 등록 request 유효성 검사 실패: 광고명 빈 값")
    @Test
    void violation_adsRequest_create_fail() {
        // given
        AdsRequest.Create request = new AdsRequest.Create("", BigDecimal.ZERO, 0, "text", "imageUrl", LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));

        // when
        Set<ConstraintViolation<AdsRequest.Create>> violations = validator.validate(request);

        // then
        assertEquals(1, violations.size());
    }

    @DisplayName("광고 참여 request 유효성 검사 성공")
    @Test
    void violation_adsJoinRequest_create_success() {
        // given
        AdsJoinRequest.Request request = new AdsJoinRequest.Request("userId", "adId");

        // when
        Set<ConstraintViolation<AdsJoinRequest.Request>> violations = validator.validate(request);

        // then
        assertEquals(0, violations.size());
    }

    @DisplayName("광고 참여 request 유효성 검사 실패: 사용자 ID, 광고 ID 빈 값")
    @Test
    void violation_adsJoinRequest_create_fail() {
        // given
        AdsJoinRequest.Request request = new AdsJoinRequest.Request("", "");

        // when
        Set<ConstraintViolation<AdsJoinRequest.Request>> violations = validator.validate(request);

        // then
        assertEquals(2, violations.size());
    }
}
