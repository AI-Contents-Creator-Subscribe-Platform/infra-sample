package org.sheep1500.toyadvertisementbackend.ads.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sheep1500.toyadvertisementbackend.ads.domain.exception.AdsValidException;
import org.springframework.util.StringUtils;

/**
 * 광고 정보 밸류
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsInfo {

    @Column(name = "ad_name", nullable = false, unique = true)
    private String name;

    @Column(name = "ad_text")
    private String text;

    public AdsInfo(String name, String text) {
        this.name = name;
        this.text = text;
    }

    private void validName() {
        if(!StringUtils.hasLength(name)) {
            throw new AdsValidException("no valid name");
        }
    }
}
