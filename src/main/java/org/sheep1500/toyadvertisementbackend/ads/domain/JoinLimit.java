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
public class JoinLimit {

    @Column(name = "join_limit")
    private Integer limit;

    @Column(name = "current_join_limit")
    private Integer currentLimit;

    public JoinLimit(Integer limit) {
        this.limit = limit;
        this.validLimit();
        this.currentLimit = limit;
    }

    private void validLimit() {
        if (this.limit == null || this.limit <= 0) {
            throw new AdsValidException("no valid limit");
        }
    }

    protected boolean enableJoin() {
        return this.currentLimit > 0;
    }

    public void reduce() {
        this.currentLimit--;
    }
}
