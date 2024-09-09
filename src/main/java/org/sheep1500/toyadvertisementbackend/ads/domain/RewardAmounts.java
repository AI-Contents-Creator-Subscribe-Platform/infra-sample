package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.exception.AdsValidException;

import java.math.BigDecimal;

/**
 * 광고 참여시 적립 액수
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RewardAmounts {
    @Column(name = "amounts")
    private BigDecimal amounts;

    public RewardAmounts(BigDecimal amounts) {
        this.amounts = amounts;
        this.validAmounts();
    }

    public void validAmounts() {
        if (this.amounts == null) {
            throw new AdsValidException("no valid amounts");
        }
    }
}
