package org.sheep1500.toyadvertisementbackend.ads_join.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinRequest {

    public record Create (@NotBlank String userId, @NotBlank String adId){

    }
}
