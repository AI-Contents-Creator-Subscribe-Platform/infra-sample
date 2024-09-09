package org.sheep1500.toyadvertisementbackend.user_ads.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AdsJoinHistoryRepository extends MongoRepository<AdsJoinHistory, String> {
    Optional<AdsJoinHistory> findByUserIdAndAdId(String userId, String adId);

    List<AdsJoinHistory> findAllByUserId(String userId);
}
