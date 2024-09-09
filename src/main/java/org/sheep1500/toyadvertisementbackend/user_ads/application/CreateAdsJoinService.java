package org.sheep1500.toyadvertisementbackend.user_ads.application;

import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.user_ads.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.user_ads.domain.AdsJoinHistory;
import org.sheep1500.toyadvertisementbackend.user_ads.mq.event.AdsJoinEvent;
import org.sheep1500.toyadvertisementbackend.user_ads.mq.producer.AdsJoinProducer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAdsJoinService {
    private final AdsJoinProducer adsJoinProducer;

    public void create(AdsJoinDto.Create dto) {
        adsJoinProducer.sendAdsJoinEvent(AdsJoinEvent.builder().build());
    }
}
