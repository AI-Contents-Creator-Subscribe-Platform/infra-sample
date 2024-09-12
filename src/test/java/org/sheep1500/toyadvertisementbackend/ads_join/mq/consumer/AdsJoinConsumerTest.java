package org.sheep1500.toyadvertisementbackend.ads_join.mq.consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sheep1500.toyadvertisementbackend.ads.application.ReduceAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.event.JoinAdsEvent;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistoryRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.external.AdsJoinRequestProxy;
import org.sheep1500.toyadvertisementbackend.ads_join.application.RequestAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.external.dto.RewardPointDto;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.fixture.AdsJoinEventFixture;
import org.sheep1500.toyadvertisementbackend.mock.BaseMockTest;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

class AdsJoinConsumerTest extends BaseMockTest {

    @Mock
    private AdsJoinHistoryRepository adsJoinHistoryRepository;
    @Mock
    private ReduceAdsJoinService reduceAdsJoinService;
    @Mock
    private RequestAdsJoinService requestAdsJoinService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private AdsJoinRequestProxy joinRequestProxy;

    @InjectMocks
    private AdsJoinConsumer adsJoinConsumer;

    @DisplayName("광고 참여 처리 성공")
    @Test
    void joinAds_success() {
        // given
        AdsJoinEvent event = AdsJoinEventFixture.createAdsJoinEvent("userId", "adId", "adName", BigDecimal.ONE, LocalDateTime.now());

        // when
        AdsJoinHistory adsJoinHistory = adsJoinConsumer.joinAds(event);

        // then
        verify(requestAdsJoinService).validJoin(event.getUserId(), event.getAdId());
        verify(reduceAdsJoinService).reduceJoinCount(new AdsId(event.getAdId()));
        verify(adsJoinHistoryRepository).save(any(AdsJoinHistory.class));
        verify(joinRequestProxy).rewardPoint(any(RewardPointDto.class));
        verify(eventPublisher).publishEvent(any(JoinAdsEvent.class));

        assertEquals(event.getUserId(), adsJoinHistory.getUserId());
        assertEquals(event.getAdId(), adsJoinHistory.getAdId());
        assertEquals(event.getAdName(), adsJoinHistory.getAdName());
        assertEquals(event.getRewardAmount(), adsJoinHistory.getRewardAmount());
        assertEquals(event.getJoinTime(), adsJoinHistory.getJoinDate());
    }
}