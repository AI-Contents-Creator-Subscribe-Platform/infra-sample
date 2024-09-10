package org.sheep1500.toyadvertisementbackend.ads.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsRequest {


    public record Create(@NotEmpty String name, BigDecimal reward, Integer joinCount, String text, String imageUrl,
                         LocalDateTime startDate, LocalDateTime endDate) {
    }
}
