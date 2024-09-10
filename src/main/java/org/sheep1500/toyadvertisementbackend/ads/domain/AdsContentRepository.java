package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AdsContentRepository extends JpaRepository<AdsContent, AdsId> {

    @Transactional(readOnly = true)
    boolean existsByInfo_name(String name);
}
