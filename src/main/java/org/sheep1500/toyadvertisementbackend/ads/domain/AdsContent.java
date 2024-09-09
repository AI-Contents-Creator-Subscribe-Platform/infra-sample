package org.sheep1500.toyadvertisementbackend.ads.domain;


import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.common.entity.BaseEntity;

@Entity
@Table(name = "T_ADS_CONTENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsContent extends BaseEntity {

    @EmbeddedId
    private AdsContentId id;

    @Embedded
    private AdsInfo info;

    @Embedded
    private AdsImage image;


    public AdsContent(AdsContentId id, AdsInfo info, AdsImage image) {
        this.id = id;
        this.info = info;
        this.image = image;
    }
}
