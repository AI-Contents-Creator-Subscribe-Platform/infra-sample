package org.sheep1500.toyadvertisementbackend.ads.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 광고 이미지 URL 밸류
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdsImage {

    @Column(name = "image_url")
    private String url;

    public AdsImage(String url) {
        this.url = url;
    }
}
