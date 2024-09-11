package org.sheep1500.toyadvertisementbackend.ads.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsContentRepository;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsRepository;
import org.sheep1500.toyadvertisementbackend.ads.event.CreateAdsEvent;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsExistNameException;
import org.sheep1500.toyadvertisementbackend.fixture.AdsDtoFixture;
import org.sheep1500.toyadvertisementbackend.mock.BaseMockTest;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

class CreateAdsServiceTest extends BaseMockTest {
    @Mock
    private AdsRepository repository;

    @Mock
    private AdsContentRepository contentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CreateAdsService createAdsService;

    @DisplayName("광고 등록 성공")
    @Test
    void createAds_success() {
        // given
        AdsDto.Create dto = AdsDtoFixture.createAdsDtoCreate("name", BigDecimal.valueOf(100L), 10, "ad text", "http://image.url", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        given(contentRepository.existsByInfo_name(dto.name())).willReturn(false);

        // when
        Ads result = createAdsService.create(dto);

        // then
        then(contentRepository).should().existsByInfo_name(dto.name());
        then(repository).should().save(result);
        then(eventPublisher).should().publishEvent(any(CreateAdsEvent.class));

        assertEquals(dto.name(), result.getContent().getInfo().getName());
        assertEquals(dto.reward(), result.getReward().getAmounts());
        assertEquals(dto.joinCount(), result.getLimit().getLimit());
        assertEquals(dto.text(), result.getContent().getInfo().getText());
        assertEquals(dto.imageUrl(), result.getContent().getImage().getUrl());
        assertEquals(dto.startDate(), result.getDisplayDate().getStartDate());
        assertEquals(dto.endDate(), result.getDisplayDate().getEndDate());
    }

    @DisplayName("광고 등록 실패: 존재하는 광고 이름")
    @Test
    void createAds_fail_existName() {
        // given
        AdsDto.Create dto = AdsDtoFixture.createAdsDtoCreate("name", BigDecimal.valueOf(100L), 10, "ad text", "http://image.url", LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        given(contentRepository.existsByInfo_name(dto.name())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> createAdsService.create(dto))
                .isInstanceOf(AdsExistNameException.class)
                .hasMessageContaining("exist ad name[" + dto.name() + "]");

        verify(contentRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any(CreateAdsEvent.class));
    }
}