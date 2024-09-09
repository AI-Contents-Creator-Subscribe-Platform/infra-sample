package org.sheep1500.toyadvertisementbackend.ads.application;

import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;
import org.sheep1500.toyadvertisementbackend.ads.domain.AdsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateAdsService {

    private final AdsRepository repository;

    @Transactional
    public Ads create(AdsDto.Create dto) {
        Ads ads = new Ads();
        return repository.save(ads);
    }
}
