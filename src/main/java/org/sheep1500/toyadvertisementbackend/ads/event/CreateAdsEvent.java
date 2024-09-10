package org.sheep1500.toyadvertisementbackend.ads.event;

import lombok.Getter;
import org.sheep1500.toyadvertisementbackend.ads.domain.Ads;

@Getter
public record CreateAdsEvent(Ads ads) {
}
