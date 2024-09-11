package org.sheep1500.toyadvertisementbackend.facade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.common.domain.IdGenerator;
import org.sheep1500.toyadvertisementbackend.lock.LockData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class LockAdsFacadeTest {

    @Autowired
    private LockAdsFacade lockAdsFacade;

    private static class ConcurrencyTest {
        private int counter = 0;

        public void increment() {
            try {
                Thread.sleep(1);  // 원할한 동시성 접근을 위한 짧은 시간 지연 추가
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }

    @DisplayName("동시성 테스트 성공: LockAdsFacade 의 Lock 적용")
    @Test
    void concurrency_withLock() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        ConcurrencyTest test = new ConcurrencyTest();
        String lockUniqueId = IdGenerator.simpleTimestampUuid();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    // lock 적용
                    lockAdsFacade.executeWithLock(new LockData(lockUniqueId), test::increment);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        latch.countDown();
        completionLatch.await(5, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        assertEquals(threadCount, test.getCounter());
    }

    @DisplayName("동시성 테스트 실패: LockAdsFacade 의 Lock 미적용")
    @Test
    void concurrency_withoutLock() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch completionLatch = new CountDownLatch(threadCount);
        ConcurrencyTest test = new ConcurrencyTest();

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                    // lock 미적용
                    test.increment();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        latch.countDown();
        completionLatch.await(5, TimeUnit.SECONDS);
        executorService.shutdown();

        // then
        assertNotEquals(threadCount, test.getCounter());
    }
}