package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdsJoinEventFixture {
    public static AdsJoinEvent createAdsJoinEvent(String userId, String adId, String adName, BigDecimal rewardAmount, LocalDateTime joinTime) {
        return AdsJoinEvent.builder()
                .userId(userId)
                .adId(adId)
                .adName(adName)
                .rewardAmount(rewardAmount)
                .joinTime(joinTime)
                .build();
    }
}
