package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.exception.AdsValidException;

/**
 * 결제금액 정보를 담는 밸류
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantLimit {

    @Column(name = "participant_limit")
    private Integer limit;

    @Column(name = "current_participant_limit")
    private Integer currentLimit;

    public ParticipantLimit(Integer limit) {
        this.limit = limit;
        this.validLimit();
        this.currentLimit = limit;
    }

    private void validLimit() {
        if (this.limit == null || this.limit <= 0) {
            throw new AdsValidException("no valid limit");
        }
    }
}
