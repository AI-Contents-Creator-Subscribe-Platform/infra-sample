package org.sheep1500.toyadvertisementbackend.ads.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsRequest {


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Create {
        @NotNull
        private BigDecimal amounts;
        @NotNull
        private BigDecimal vat;
    }
}
