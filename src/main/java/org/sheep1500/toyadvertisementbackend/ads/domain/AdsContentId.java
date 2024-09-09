package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 광고 Content ID(unique) 밸류
 */
@Embeddable
@Getter
public class AdsContentId implements Serializable {
    @Column(name = "ads_content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdsContentId adsContentId = (AdsContentId) o;
        return Objects.equals(id, adsContentId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
