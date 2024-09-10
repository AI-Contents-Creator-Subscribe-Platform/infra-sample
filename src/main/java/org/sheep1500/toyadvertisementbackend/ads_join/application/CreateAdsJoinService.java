package org.sheep1500.toyadvertisementbackend.ads_join.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.JoinExistException;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.producer.AdsJoinProducer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateAdsJoinService {
    private final AdsJoinProducer adsJoinProducer;
    private final QueryAdsService queryAdsService;
    private final QueryAdsJoinService queryAdsJoinService;

    public void create(AdsJoinDto.Create dto) {
        // 참여가능한지 조회
        this.validJoin(dto.userId(), dto.adId());

        adsJoinProducer.sendJoinAdsEvent(AdsJoinEvent.builder()
                .userId(dto.userId())
                .adId(dto.adId())
                .build());
    }

    public void validJoin(String userId, String adId) {
        Ads ads = queryAdsService.getAds(new AdsId(adId))
                .orElseThrow(EntityNotFoundException::new);

        if (!ads.enableJoin()) {
            throw new DisableJoinAdsException();
        }

        queryAdsJoinService.getByUserIdAndAdId(userId, adId)
                .ifPresent(o -> {
                    throw new JoinExistException();
                });
    }
}
