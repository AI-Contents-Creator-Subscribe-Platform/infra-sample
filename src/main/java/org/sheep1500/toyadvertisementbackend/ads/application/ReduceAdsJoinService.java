package org.sheep1500.toyadvertisementbackend.ads.application;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsId;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsRepository;
import org.sheep1500.toyadvertisementbackend.ads_join.exception.DisableJoinAdsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReduceAdsJoinService {

    private final AdsRepository repository;

    @Transactional
    public void reduceJoinCount(AdsId adsId) {
        Ads ads = repository.findById(adsId).orElseThrow(() -> new EntityNotFoundException("ads not exist"));
        if (!ads.enableJoin()) {
            throw new DisableJoinAdsException("current limit over");
        }

        ads.getLimit().reduce();
    }
}
