package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.ads.domain.fixture.AdsIdFixture;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;

import static org.junit.jupiter.api.Assertions.*;

class AdsIdTest {

    @DisplayName("광고 ID 검증 성공: UUID 기반 아이디")
    @Test
    void validAdsId_success() {
        String id = IdGenerator.simpleTimestampUuid();
        AdsId adsId = AdsIdFixture.createAdsId(id);
        assertEquals(id, adsId.getId());
    }

    @DisplayName("광고 ID 검증 실패: 빈값")
    @Test
    void validAdsId_fail_empty() {
        Throwable exception = assertThrows(AdsValidException.class, () -> AdsIdFixture.createAdsId(""));
        assertEquals("no valid ads id", exception.getMessage());
    }
}