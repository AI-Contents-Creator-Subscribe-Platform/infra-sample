package org.sheep1500.toyadvertisementbackend.ads_join.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sheep1500.toyadvertisementbackend.ads.domain.*;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.AdsJoinExistException;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.producer.AdsJoinProducer;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.sheep1500.toyadvertisementbackend.fixture.*;
import org.sheep1500.toyadvertisementbackend.mock.BaseMockTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class RequestAdsJoinServiceTest extends BaseMockTest {
    @Mock
    private AdsJoinProducer adsJoinProducer;

    @Mock
    private QueryAdsService queryAdsService;

    @Mock
    private QueryAdsJoinService queryAdsJoinService;

    @InjectMocks
    private RequestAdsJoinService requestAdsJoinService;

    @DisplayName("광고 참여 요청 성공")
    @Test
    void requestJoinAd_success() {
        // given
        AdsId adsId = new AdsId(IdGenerator.simpleTimestampUuid());
        AdsJoinDto.Create dto = AdsJoinDtoFixture.createAdsJoinDtoCreate("userId", adsId.getId());
        Ads ads = AdsFixture.createAds(
                adsId,
                RewardAmountsFixture.createRewardAmounts(BigDecimal.valueOf(100)),
                JoinLimitFixture.createJoinLimit(10),
                null,
                new AdsContent(adsId, AdsInfoFixture.createAdsInfo("name", "text"), new AdsImage("image"))
        );

        given(queryAdsService.getAds(adsId)).willReturn(Optional.ofNullable(ads));
        given(queryAdsJoinService.getByUserIdAndAdId(any(), any())).willReturn(Optional.empty());

        // when
        requestAdsJoinService.requestJoinAd(dto);

        // then
        then(adsJoinProducer).should().sendJoinAdsEvent(any(AdsJoinEvent.class));
    }

    @DisplayName("광고 참여 요청 실패: 광고 존재 안함")
    @Test
    void requestJoinAd_fail_notExistAds() {
        // given
        AdsId adsId = new AdsId(IdGenerator.simpleTimestampUuid());
        AdsJoinDto.Create dto = AdsJoinDtoFixture.createAdsJoinDtoCreate("userId", adsId.getId());
        given(queryAdsService.getAds(adsId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> requestAdsJoinService.requestJoinAd(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ads not exist");

        verify(queryAdsJoinService, never()).getByUserIdAndAdId(any(), any());
        verify(adsJoinProducer, never()).sendJoinAdsEvent(any());
    }

    @DisplayName("광고 참여 요청 실패: 참여횟수 없음")
    @Test
    void requestJoinAd_fail_enableJoin() {
        // given
        AdsId adsId = new AdsId(IdGenerator.simpleTimestampUuid());
        Ads ads = mock(Ads.class);
        AdsJoinDto.Create dto = AdsJoinDtoFixture.createAdsJoinDtoCreate("userId", adsId.getId());
        given(ads.enableJoin()).willReturn(false);
        given(queryAdsService.getAds(adsId)).willReturn(Optional.of(ads));

        // when & then
        assertThatThrownBy(() -> requestAdsJoinService.requestJoinAd(dto))
                .isInstanceOf(DisableJoinAdsException.class)
                .hasMessageContaining("current limit over");

        verify(queryAdsJoinService, never()).getByUserIdAndAdId(any(), any());
        verify(adsJoinProducer, never()).sendJoinAdsEvent(any());
    }

    @DisplayName("광고 참여 요청 실패: 참여이력존재")
    @Test
    void requestJoinAd_fail_existJoinHistory() {
        // given
        AdsId adsId = new AdsId(IdGenerator.simpleTimestampUuid());
        Ads ads = mock(Ads.class);
        AdsJoinDto.Create dto = AdsJoinDtoFixture.createAdsJoinDtoCreate("userId", adsId.getId());
        given(ads.enableJoin()).willReturn(true);
        given(ads.getId()).willReturn(adsId);
        given(queryAdsService.getAds(adsId)).willReturn(Optional.of(ads));
        given(queryAdsJoinService.getByUserIdAndAdId(any(), any())).willReturn(Optional.of(mock()));

        // when & then
        assertThatThrownBy(() -> requestAdsJoinService.requestJoinAd(dto))
                .isInstanceOf(AdsJoinExistException.class);

        verify(adsJoinProducer, never()).sendJoinAdsEvent(any());
    }
}