package org.sheep1500.toyadvertisementbackend.ads_join.domain;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ads_join_history")
@CompoundIndexes({
        @CompoundIndex(name = "user_ad_idx", def = "{'user_id' : 1, 'ad_id' : 1}"),
        @CompoundIndex(name = "user_join_date_idx", def = "{'user_id' : 1, 'join_date' : -1}")
})
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsJoinHistory {
    @Indexed
    private String userId;
    private String adId;
    private String adName;
    private double rewardAmount;
    private LocalDateTime joinDate;

    @Builder
    public AdsJoinHistory(String userId, String adId, String adName, double rewardAmount, LocalDateTime joinDate) {
        this.userId = userId;
        this.adId = adId;
        this.adName = adName;
        this.rewardAmount = rewardAmount;
        this.joinDate = joinDate;
    }
}
