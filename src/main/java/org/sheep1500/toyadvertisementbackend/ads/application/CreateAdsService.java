package org.sheep1500.toyadvertisementbackend.ads.application;

import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;
import org.sheep1500.toyadvertisementbackend.ads.domain.*;
import org.sheep1500.toyadvertisementbackend.ads.event.CreateAdsEvent;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAdsService {

    private final AdsRepository repository;

    private final ApplicationEventPublisher eventPublisher;

    private AdsId createAdsId() {
        String id = "ads" + IdGenerator.simpleTimestampUuid();
        return new AdsId(id);
    }

    @Transactional
    public Ads create(AdsDto.Create dto) {
        Ads ads = Ads.builder()
                .id(createAdsId())
                .reward(new RewardAmounts(dto.reward()))
                .content(AdsContent.builder()
                        .info(new AdsInfo(dto.name(), dto.text()))
                        .image(new AdsImage(dto.imageUrl()))
                        .build())
                .limit(new JoinLimit(dto.joinCount()))
                .displayDate(new AdsDisplayDate(dto.startDate(), dto.endDate()))
                .build();

        repository.save(ads);

        eventPublisher.publishEvent(new CreateAdsEvent(ads));

        return ads;
    }
}
