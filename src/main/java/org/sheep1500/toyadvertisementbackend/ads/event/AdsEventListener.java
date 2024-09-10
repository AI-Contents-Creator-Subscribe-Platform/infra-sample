package org.sheep1500.toyadvertisementbackend.ads.event;


import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.QueryAdsService;
import org.sheep1500.toyadvertisementbackend.facade.CacheAdsFacade;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdsEventListener {

    private final CacheAdsFacade cacheAdsFacade;

    @EventListener
    public void createAdsEvent(CreateAdsEvent event) {
        Ads ads = event.ads();

        if (!ads.isBetweenDate(QueryAdsService.currentDateTime())) return;

        cacheAdsFacade.evictCurrentDisplayAds(QueryAdsService.currentDateString());
    }

    @EventListener
    public void joinAdsEvent(JoinAdsEvent event) {
        Ads ads = event.ads();

        if (ads.enableJoin()) return;

        cacheAdsFacade.evictCurrentDisplayAds(QueryAdsService.currentDateString());
    }
}
