package org.sheep1500.toyadvertisementbackend.ads.application.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsDto {

    @Builder
    public record Create(String name, BigDecimal reward, Integer joinCount, String text, String imageUrl,
                         LocalDateTime startDate, LocalDateTime endDate) {
    }
}
