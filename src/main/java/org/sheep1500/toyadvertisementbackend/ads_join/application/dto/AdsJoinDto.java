package org.sheep1500.toyadvertisementbackend.ads_join.application.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Create {
        private String userId;
        private String adId;
    }
}
