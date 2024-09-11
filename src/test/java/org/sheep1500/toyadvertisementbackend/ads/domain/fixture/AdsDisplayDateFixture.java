package org.sheep1500.toyadvertisementbackend.ads.domain.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.*;

import java.time.LocalDateTime;

public class AdsDisplayDateFixture {
    public static AdsDisplayDate createAdsDisplayDate(LocalDateTime startDate, LocalDateTime endDate) {
        return new AdsDisplayDate(startDate, endDate);
    }
}
