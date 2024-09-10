package org.sheep1500.toyadvertisementbackend.ads.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.common.entity.BaseEntity;

@Entity
@Table(name = "T_ADS",
        indexes = @Index(name = "t_ads_indexes", columnList = "display_start_date, display_end_date, current_join_limit")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ads extends BaseEntity {

    @EmbeddedId
    @Getter
    private AdsId id;

    @Embedded
    private RewardAmounts reward;

    @Embedded
    @Getter
    private JoinLimit limit;

    @Embedded
    @Getter
    private AdsDisplayDate displayDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "adsId", referencedColumnName = "ads_id")
    private AdsContent content;

    @Builder
    public Ads(AdsId id, RewardAmounts reward, JoinLimit limit, AdsDisplayDate displayDate, AdsContent content) {
        this.id = id;
        this.reward = reward;
        this.limit = limit;
        this.displayDate = displayDate;
        this.content = content;
    }

    public boolean enableJoin() {
        return this.limit.enableJoin();
    }
}
