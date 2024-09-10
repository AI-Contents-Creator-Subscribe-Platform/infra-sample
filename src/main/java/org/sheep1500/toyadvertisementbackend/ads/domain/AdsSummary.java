package org.sheep1500.toyadvertisementbackend.ads.domain;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class AdsSummary implements Serializable {
    private String adsId;
    private String adsName;
    private String adsDescription;
    private String imageUrl;
    private BigDecimal reward;

    @Builder
    public AdsSummary(String adsId, String adsName, String adsDescription, String imageUrl, BigDecimal reward) {
        this.adsId = adsId;
        this.adsName = adsName;
        this.adsDescription = adsDescription;
        this.imageUrl = imageUrl;
        this.reward = reward;
    }
}
