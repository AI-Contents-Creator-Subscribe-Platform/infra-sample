package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads.application.dto.AdsDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdsDtoFixture {

    public static AdsDto.Create createAdsDtoCreate(String name, BigDecimal reward, int joinCount, String text, String imageUrl, LocalDateTime startDate, LocalDateTime endDate) {
        return AdsDto.Create.builder()
                .name(name)
                .reward(reward)
                .joinCount(joinCount)
                .text(text)
                .imageUrl(imageUrl)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
