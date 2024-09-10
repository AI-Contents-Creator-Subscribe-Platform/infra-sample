package org.sheep1500.toyadvertisementbackend.ads_join.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.ads_join.domain.QueryAdsJoinService;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.AdsJoinExistException;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.ads_join.mq.producer.AdsJoinProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateAdsJoinService {
    private final AdsJoinProducer adsJoinProducer;
    private final QueryAdsService queryAdsService;
    private final QueryAdsJoinService queryAdsJoinService;

    @Transactional
    public void create(AdsJoinDto.Create dto) {
        // 참여가능한지 조회
        Ads ads = queryAdsService.getAds(new AdsId(dto.adId()))
                .orElseThrow(() -> new EntityNotFoundException("ads not exist"));

        this.validJoin(dto.userId(), ads);

        adsJoinProducer.sendJoinAdsEvent(AdsJoinEvent.builder()
                .userId(dto.userId())
                .adId(dto.adId())
                .adName(ads.getContent().getInfo().getName())
                .rewardAmount(ads.getReward().getAmounts())
                .joinTime(LocalDateTime.now())
                .build());
    }

    public void validJoin(String userId, Ads ads) {
        if (!ads.enableJoin()) {
            throw new DisableJoinAdsException("current limit over");
        }

        queryAdsJoinService.getByUserIdAndAdId(userId, ads.getId().getId())
                .ifPresent(o -> {
                    throw new AdsJoinExistException();
                });
    }

    public void validJoin(String userId, String adId) {
        Ads ads = queryAdsService.getAds(new AdsId(adId))
                .orElseThrow(() -> new EntityNotFoundException("ads not exist"));

        if (!ads.enableJoin()) {
            throw new DisableJoinAdsException("current limit over");
        }

        queryAdsJoinService.getByUserIdAndAdId(userId, adId)
                .ifPresent(o -> {
                    throw new AdsJoinExistException();
                });
    }
}
