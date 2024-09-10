package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AdsRepository extends JpaRepository<Ads, AdsId> {

    @Query(value =
            " select ads" +
            " from Ads ads" +
            " where 1=1 " +
            " and :currentDateTime BETWEEN ads.displayDate.startDate and ads.displayDate.endDate" +
            " and ads.limit.currentLimit > 0" +
            " order by ads.reward.amounts desc limit 10")
    List<Ads> currentDisplayAdsList(LocalDateTime currentDateTime);

}
