package org.sheep1500.toyadvertisementbackend.ads_join.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryAdsJoinService {

    private final AdsJoinHistoryRepository repository;

    @Transactional(readOnly = true)
    public Page<AdsJoinHistory> listByUserId(String userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<AdsJoinHistory> getByUserIdAndAdId(String userId, String adId) {
        return repository.findByUserIdAndAdId(userId, adId);
    }
}
