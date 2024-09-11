package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.fixture.AdsDisplayDateFixture;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AdsDisplayDateTest {

    LocalDateTime startDate;

    @BeforeEach
    public void setup() {
        startDate = LocalDateTime.now();
    }

    @DisplayName("광고 전시 시간 검증 성공")
    @Test
    void validAdsDisplayDate_success() {
        LocalDateTime endDate = startDate.plusMinutes(1);
        AdsDisplayDate adsDisplayDate = AdsDisplayDateFixture.createAdsDisplayDate(startDate, endDate);

        assertEquals(startDate, adsDisplayDate.getStartDate());
        assertEquals(endDate, adsDisplayDate.getEndDate());
    }

    @DisplayName("광고 전시 시간 검증 실패: 시작 시간 null")
    @Test
    void validAdsDisplayDate_fail_startDate_null() {
        LocalDateTime endDate = startDate.plusMinutes(1);

        Throwable exception = assertThrows(AdsValidException.class, () -> AdsDisplayDateFixture.createAdsDisplayDate(null, endDate));
        assertEquals("no valid startDate", exception.getMessage());
    }

    @DisplayName("광고 전시 시간 검증 실패: 종료 시간이 시작 시간 이전")
    @Test
    void validAdsDisplayDate_fail_endDate_before_StartDate() {
        LocalDateTime endDate = startDate.minusSeconds(1);

        Throwable exception = assertThrows(AdsValidException.class, () -> AdsDisplayDateFixture.createAdsDisplayDate(startDate, endDate));
        assertEquals("no valid endDate", exception.getMessage());
    }

    @DisplayName("광고 전시 시간 비교 시간이 검증 시간 사이 true")
    @Test
    void isBetween_true() {
        // given
        LocalDateTime endDate = startDate.plusMinutes(2);
        LocalDateTime compareDate = endDate.minusMinutes(1);
        AdsDisplayDate adsDisplayDate = AdsDisplayDateFixture.createAdsDisplayDate(startDate, endDate);

        // when
        boolean isBetween = adsDisplayDate.isBetween(compareDate);

        // then
        assertTrue(isBetween);
    }

    @DisplayName("광고 전시 시간 비교 시간이 검증 시간 사이 false")
    @Test
    void isBetween_false() {
        // given
        LocalDateTime endDate = startDate.plusMinutes(2);
        LocalDateTime compareDate = endDate.plusSeconds(1);
        AdsDisplayDate adsDisplayDate = AdsDisplayDateFixture.createAdsDisplayDate(startDate, endDate);

        // when
        boolean isBetween = adsDisplayDate.isBetween(compareDate);

        // then
        assertFalse(isBetween);
    }
}