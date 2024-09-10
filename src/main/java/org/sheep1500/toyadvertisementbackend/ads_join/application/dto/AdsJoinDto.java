package org.sheep1500.toyadvertisementbackend.ads_join.application.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdsJoinDto {

    public record Create (String userId, String adId){

    }
}
