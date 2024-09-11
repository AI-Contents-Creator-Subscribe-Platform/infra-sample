package org.sheep1500.toyadvertisementbackend.ads.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.sheep1500.toyadvertisementbackend.ads.domain.fixture.JoinLimitFixture;
import org.sheep1500.toyadvertisementbackend.ads.exception.AdsValidException;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class JoinLimitTest {
    @DisplayName("광고 참여 인원 검증 성공")
    @Test
    void validJoinLimit_success() {
        int count = 10;
        JoinLimit joinLimit = JoinLimitFixture.createJoinLimit(count);
        assertEquals(count, joinLimit.getLimit());
        assertEquals(count, joinLimit.getCurrentLimit());
    }

    @DisplayName("광고 참여 인원 검증 실패: 0 이하")
    @Test
    void validJoinLimit_less_ZERO() {
        int count = 10;
        Throwable exception = assertThrows(AdsValidException.class, () -> JoinLimitFixture.createJoinLimit(count));
        assertEquals("no valid limit", exception.getMessage());
    }

    @DisplayName("광고 참여 인원 기존 인원보다 적게 감소 후 참여 가능 true")
    @Test
    void enableJoin_success() {
        // given
        int count = 10;
        JoinLimit joinLimit = JoinLimitFixture.createJoinLimit(count);

        // when
        // count -1 만큼 반복
        IntStream.range(1, count).forEach(i -> joinLimit.reduce());

        // then
        assertTrue(joinLimit.enableJoin());
        assertEquals(count, joinLimit.getLimit());
        assertEquals(1, joinLimit.getCurrentLimit());
    }

    @DisplayName("광고 참여 인원 기존 인원보다 많게 감소 후 참여 가능 false")
    @Test
    void enableJoin_false() {
        // given
        int count = 10;
        JoinLimit joinLimit = JoinLimitFixture.createJoinLimit(count);

        // when
        // count 만큼 반복
        IntStream.rangeClosed(1, count).forEach(i -> joinLimit.reduce());

        // then
        assertFalse(joinLimit.enableJoin());
        assertEquals(count, joinLimit.getLimit());
        assertEquals(0, joinLimit.getCurrentLimit());
    }
}