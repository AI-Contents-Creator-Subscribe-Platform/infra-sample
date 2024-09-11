package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.*;

public class AdsFixture {
    public static Ads createAds(AdsId id, RewardAmounts reward, JoinLimit limit, AdsDisplayDate displayDate, AdsContent content) {
        return Ads.builder()
                .id(id)
                .reward(reward)
                .limit(limit)
                .displayDate(displayDate)
                .content(content)
                .build();
    }
}
