package org.sheep1500.toyadvertisementbackend.ads_join.domain;


import lombok.RequiredArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads_join.application.dto.AdsJoinDto;
import org.sheep1500.toyadvertisementbackend.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryAdsJoinService {

    private final AdsJoinHistoryRepository repository;

    @Transactional(readOnly = true)
    public PageResponse<AdsJoinHistory> listByUserId(AdsJoinDto.JoinHistoryList dto, Pageable pageable) {
        return new PageResponse<>(repository.findAllByUserIdAndJoinDateBetween(dto.userId(), dto.startDate(), dto.endDate(), pageable));
    }

    @Transactional(readOnly = true)
    public Optional<AdsJoinHistory> getByUserIdAndAdId(String userId, String adId) {
        return repository.findByUserIdAndAdId(userId, adId);
    }
}
