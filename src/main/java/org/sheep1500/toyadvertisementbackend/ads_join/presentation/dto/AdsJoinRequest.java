package org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinRequest {

    public record Create(@NotEmpty String userId, @NotEmpty String adId) {

    }
}
