package org.sheep1500.toyadvertisementbackend.ads.event;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.facade.CacheAdsFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdsEventListener {

    private final CacheAdsFacade cacheAdsFacade;
    private final QueryAdsService queryAdsService;

    @EventListener
    public void createAdsEvent(CreateAdsEvent event) {
        Ads ads = event.ads();

        if (!ads.isBetweenDate(QueryAdsService.currentDateTime())) return;

        cacheAdsFacade.evictCurrentDisplayAds(QueryAdsService.currentDateString());
    }

    @EventListener
    public void joinAdsEvent(JoinAdsEvent event) {
        Ads ads = queryAdsService.getAds(event.adsId())
                .orElseThrow(() -> new EntityNotFoundException("ads not found"));

        if (ads.enableJoin()) return;

        cacheAdsFacade.evictCurrentDisplayAds(QueryAdsService.currentDateString());
    }
}
