package org.sheep1500.toyadvertisementbackend.ads.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryAdsService {

    private final AdsRepository repository;

    @Transactional(readOnly = true)
    public List<Ads> search(AdsId adsId) {
        return repository.findAll();
    }
}
