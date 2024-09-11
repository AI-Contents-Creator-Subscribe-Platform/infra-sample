package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.AdsInfo;

public class AdsInfoFixture {
    public static AdsInfo createAdsInfo(String name, String text) {
        return new AdsInfo(name, text);
    }
}
