package org.sheep1500.toyadvertisementbackend.ads.application;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.sheep1500.toyadvertisementbackend.fixture.AdsFixture;
import org.sheep1500.toyadvertisementbackend.fixture.AdsIdFixture;
import org.sheep1500.toyadvertisementbackend.fixture.JoinLimitFixture;
import org.sheep1500.toyadvertisementbackend.mock.BaseMockTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ReduceAdsJoinServiceTest extends BaseMockTest {
    @Mock
    private AdsRepository repository;

    @InjectMocks
    private ReduceAdsJoinService reduceAdsJoinService;

    @DisplayName("광고 참여 횟수 차감 성공")
    @Test
    void reduceJoinCount_success() {
        // given
        AdsId adsId = AdsIdFixture.createAdsId(IdGenerator.simpleTimestampUuid());
        int limit = 10;
        Ads ads = AdsFixture.createAds(
                adsId,
                null,
                JoinLimitFixture.createJoinLimit(limit),
                null,
                null
        );

        given(repository.findById(adsId)).willReturn(Optional.of(ads));

        // when
        reduceAdsJoinService.reduceJoinCount(adsId);

        // then
        assertEquals(limit, ads.getLimit().getLimit());
        assertEquals(limit-1, ads.getLimit().getCurrentLimit());
        assertEquals(adsId, ads.getId());
    }

    @DisplayName("광고 참여 횟수 차감 실패: entity 존재 안함")
    @Test
    void reduceJoinCount_fail_notExist() {
        // given
        given(repository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reduceAdsJoinService.reduceJoinCount(AdsIdFixture.createAdsId("test")))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ads not exist");
    }

    @DisplayName("광고 참여 횟수 차감 실패: 잔여 참여횟수 없음")
    @Test
    void reduceJoinCount_fail_enableJoin() {
        // given
        Ads ads = mock(Ads.class);
        given(ads.enableJoin()).willReturn(false);
        given(repository.findById(any())).willReturn(Optional.of(ads));

        // when & then
        assertThatThrownBy(() -> reduceAdsJoinService.reduceJoinCount(AdsIdFixture.createAdsId("test")))
                .isInstanceOf(DisableJoinAdsException.class)
                .hasMessageContaining("current limit over");
    }
}