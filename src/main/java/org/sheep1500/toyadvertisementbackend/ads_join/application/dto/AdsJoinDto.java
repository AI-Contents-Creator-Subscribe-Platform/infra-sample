package org.sheep1500.toyadvertisementbackend.ads_join.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinDto {

    public record Create (String userId, String adId){

    }
    public record JoinHistoryList(@NotEmpty String userId, @NotNull LocalDateTime startDate,
                                  @NotNull LocalDateTime endDate) {
    }
}
