package org.sheep1500.toyadvertisementbackend.ads_join.mq.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdsJoinEvent {
    private String userId;
    private String adId;
    private String adName;
    private double rewardAmount;
    private LocalDateTime joinTime;
}