package org.sheep1500.toyadvertisementbackend.ads.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.common.entity.BaseEntity;

@Entity
@Table(name = "T_ADS_CONTENT", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ad_name")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AdsContent {

    @EmbeddedId
    private AdsId adsId;

    @Embedded
    private AdsInfo info;

    @Embedded
    private AdsImage image;


    @Builder
    public AdsContent(AdsId adsId, AdsInfo info, AdsImage image) {
        this.adsId = adsId;
        this.info = info;
        this.image = image;
    }
}
