package org.sheep1500.toyadvertisementbackend.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
@SpringBootTest
class CacheAdsFacadeTest {
    @Autowired
    private CacheAdsFacade cacheAdsFacade;

    private static class CachingTest {
        private int counter = 0;

        public void increment() {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }
    @DisplayName("Caching 적용 성공: 캐싱 후에 추가 메소드 실행 시 동일 value return")
    @Test
    void caching_success() {
        // given
        String cachingUniqueId = IdGenerator.simpleTimestampUuid();
        CachingTest test = new CachingTest();

        // when
        int result = cacheAdsFacade.cacheCurrentDisplayAds(cachingUniqueId, () -> {
            test.increment();
            return test.getCounter();
        });

        int resultAfterCaching = cacheAdsFacade.cacheCurrentDisplayAds(cachingUniqueId, () -> {
            test.increment();
            return test.getCounter();
        });
        cacheAdsFacade.evictCurrentDisplayAds(cachingUniqueId);

        // then
        assertEquals(result, resultAfterCaching);
    }

    @DisplayName("Caching 적용 실패: 캐싱 -> evict 후에 메소드 실행 시 다른 value return")
    @Test
    void notCaching_after_evict() {
        // given
        String cachingUniqueId = IdGenerator.simpleTimestampUuid();
        CachingTest test = new CachingTest();

        // when
        int result = cacheAdsFacade.cacheCurrentDisplayAds(cachingUniqueId, () -> {
            test.increment();
            return test.getCounter();
        });

        cacheAdsFacade.evictCurrentDisplayAds(cachingUniqueId);
        int resultAfterCaching = cacheAdsFacade.cacheCurrentDisplayAds(cachingUniqueId, () -> {
            test.increment();
            return test.getCounter();
        });
        cacheAdsFacade.evictCurrentDisplayAds(cachingUniqueId);

        // then
        assertNotEquals(result, resultAfterCaching);
    }
}