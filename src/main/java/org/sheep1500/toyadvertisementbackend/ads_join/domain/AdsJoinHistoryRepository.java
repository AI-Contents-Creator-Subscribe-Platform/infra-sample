package org.sheep1500.toyadvertisementbackend.ads_join.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AdsJoinHistoryRepository extends MongoRepository<AdsJoinHistory, String> {
    Optional<AdsJoinHistory> findByUserIdAndAdId(String userId, String adId);

    Page<AdsJoinHistory> findAllByUserId(String userId, Pageable pageable);
}
