package org.sheep1500.toyadvertisementbackend.user_ads.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ads_join_history")
@CompoundIndexes({
        @CompoundIndex(name = "user_ad_idx", def = "{'user_id' : 1, 'ad_id' : 1}"),
        @CompoundIndex(name = "user_join_time_idx", def = "{'user_id' : 1, 'join_time' : -1}")
})
@Data
public class AdsJoinHistory {
    @Indexed
    private String userId;
    private String adId;
    private String adName;
    private double rewardAmount;
    private LocalDateTime joinTime;
}
