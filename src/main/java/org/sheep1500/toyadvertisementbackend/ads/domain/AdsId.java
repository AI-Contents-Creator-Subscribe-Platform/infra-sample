package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.exception.AdsValidException;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 광고 ID(unique) 밸류
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsId implements Serializable {
    @Column(name = "ads_id")
    private String id;

    public AdsId(String id) {
        this.validAssignmentId(id);
        this.id = id;
    }

    private void validAssignmentId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new AdsValidException("no valid ads id");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdsId adsId = (AdsId) o;
        return Objects.equals(id, adsId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
