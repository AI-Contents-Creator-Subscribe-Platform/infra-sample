package org.sheep1500.toyadvertisementbackend.ads.application.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsDto {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Create {
        private BigDecimal amounts;
        private BigDecimal vat;
    }
}
