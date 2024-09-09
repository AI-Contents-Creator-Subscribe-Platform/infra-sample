package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdsRepository extends JpaRepository<Ads, AdsId> {
}
