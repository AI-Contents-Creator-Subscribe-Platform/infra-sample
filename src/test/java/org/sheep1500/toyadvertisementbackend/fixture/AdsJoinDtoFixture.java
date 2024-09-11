package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;

public class AdsJoinDtoFixture {
    public static AdsJoinDto.Create createAdsJoinDtoCreate(String userId, String adId) {
        return new AdsJoinDto.Create(userId, adId);
    }
}
