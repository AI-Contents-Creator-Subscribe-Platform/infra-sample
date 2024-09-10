package org.sheep1500.toyadvertisementbackend.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CacheAdsFacade {

    @Cacheable(value = "ads_current", key = "#cacheKey")
    public <T> T cacheCurrentDisplayAds(String cacheKey, Supplier<T> supplier) {
        return supplier.get();
    }

    @CacheEvict(value = "ads_current", key = "#cacheKey")
    public void evictCurrentDisplayAds(String cacheKey) {
    }
}
