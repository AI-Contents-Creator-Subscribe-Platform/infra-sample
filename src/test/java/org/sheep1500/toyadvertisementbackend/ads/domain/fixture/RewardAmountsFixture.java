package org.sheep1500.toyadvertisementbackend.ads.domain.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.RewardAmounts;

import java.math.BigDecimal;

public class RewardAmountsFixture {
    public static RewardAmounts createRewardAmounts(BigDecimal amount) {
        return new RewardAmounts(amount);
    }
}
