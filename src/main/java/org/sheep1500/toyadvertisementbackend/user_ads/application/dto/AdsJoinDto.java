package org.sheep1500.toyadvertisementbackend.user_ads.application.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Create {
        private BigDecimal amounts;
        private BigDecimal vat;
    }
}
