package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;

import java.time.LocalDateTime;

/**
 * 광고 노출 기간 밸류
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsDisplayDate {

    @Column(name = "display_start_date")
    private LocalDateTime startDate;

    @Column(name = "display_end_date")
    private LocalDateTime endDate;

    public AdsDisplayDate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.validStartDate();
        this.endDate = endDate;
        this.validEndDate();
    }

    private void validStartDate() {
        if (this.startDate == null) {
            throw new AdsValidException("no valid startDate");
        }
    }

    private void validEndDate() {
        if (this.endDate == null || this.startDate.isAfter(this.endDate)) {
            throw new AdsValidException("no valid endDate");
        }
    }

    protected boolean isBetween(LocalDateTime date) {
        return this.startDate.isBefore(date) && this.endDate.isAfter(date);
    }
}
