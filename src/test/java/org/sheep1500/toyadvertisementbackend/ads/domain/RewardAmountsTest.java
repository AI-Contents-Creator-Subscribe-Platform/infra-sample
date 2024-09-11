package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.fixture.RewardAmountsFixture;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RewardAmountsTest {
    @DisplayName("광고 참여 적립 포인트 검증 성공")
    @Test
    void validRewardAmounts_success() {
        RewardAmounts rewardAmounts = RewardAmountsFixture.createRewardAmounts(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), rewardAmounts.getAmounts());
    }

    @DisplayName("광고 참여 적립 포인트 검증 실패: null")
    @Test
    void validRewardAmounts_fail() {
        Throwable exception = assertThrows(AdsValidException.class, () -> RewardAmountsFixture.createRewardAmounts(null));
        assertEquals("no valid amounts", exception.getMessage());
    }
}