package org.sheep1500.toyadvertisementbackend.user_ads.domain;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryAdsJoinService {

    private final AdsJoinHistoryRepository repository;

    @Transactional(readOnly = true)
    public List<AdsJoinHistory> list(String userId) {
        return repository.findAllByUserId(userId);
    }
}
