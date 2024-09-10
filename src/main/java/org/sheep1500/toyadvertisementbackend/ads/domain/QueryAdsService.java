package org.sheep1500.toyadvertisementbackend.ads.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueryAdsService {

    private final AdsRepository repository;

    @Transactional(readOnly = true)
    public List<Ads> currentDisplayAdsList() {
        return repository.currentDisplayAdsList(currentDate());
    }

    @Transactional(readOnly = true)
    public Optional<Ads> getAds(AdsId adsId) {
        return repository.findById(adsId);
    }

    public static LocalDate currentDate() {
        return LocalDate.now();
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    public static String currentDateString() {
        return currentDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}