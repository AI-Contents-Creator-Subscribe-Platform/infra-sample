package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.ads.domain.fixture.AdsInfoFixture;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;

import static org.junit.jupiter.api.Assertions.*;

class AdsInfoTest {

    @DisplayName("광고 정보 검증 성공")
    @Test
    void validAdsInfo_success() {
        AdsInfo adsInfo = AdsInfoFixture.createAdsInfo("ads name", "text");
        assertNotNull(adsInfo);
    }

    @DisplayName("광고 정보 검증 실패: name 빈값")
    @Test
    void validAdsInfo_fail_empty() {
        Throwable exception = assertThrows(AdsValidException.class, () -> AdsInfoFixture.createAdsInfo("", null));
        assertEquals("no valid name", exception.getMessage());
    }
}