package org.sheep1500.toyadvertisementbackend.ads.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.common.entity.BaseEntity;

@Entity
@Table(name = "T_ADS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ads extends BaseEntity {

    @EmbeddedId
    @Getter
    private AdsId id;

    @Embedded
    private RewardAmounts reward;

    @Embedded
    private ParticipantLimit limit;

    @Embedded
    private AdsDisplayDate displayDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_content_id")
    private AdsContent content;

    public Ads(AdsId id, RewardAmounts reward, ParticipantLimit limit, AdsDisplayDate displayDate, AdsContent content) {
        this.id = id;
        this.reward = reward;
        this.limit = limit;
        this.displayDate = displayDate;
        this.content = content;
    }
}
