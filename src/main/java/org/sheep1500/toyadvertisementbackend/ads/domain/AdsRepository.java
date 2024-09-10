package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AdsRepository extends JpaRepository<Ads, AdsId> {

    @Query(value = "" +
            " select ads" +
            " from Ads ads" +
            " where 1=1 " +
            " and :currentDate BETWEEN ads.displayDate.startDate and ads.displayDate.endDate" +
            " order by ads.reward.amounts desc limit 10")
    List<Ads> currentDisplayAdsList(LocalDate currentDate);
}
