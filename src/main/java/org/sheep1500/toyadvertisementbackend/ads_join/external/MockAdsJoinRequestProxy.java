package org.sheep1500.toyadvertisementbackend.ads_join.external;

import lombok.extern.slf4j.Slf4j;
import org.sheep1500.toyadvertisementbackend.ads_join.external.dto.RewardPointDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MockAdsJoinRequestProxy implements AdsJoinRequestProxy{
    @Override
    public void rewardPoint(RewardPointDto dto) {
        log.info("mock rewardPoint [{}]", dto);
    }
}
