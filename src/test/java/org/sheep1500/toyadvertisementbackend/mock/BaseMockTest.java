package org.sheep1500.toyadvertisementbackend.mock;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

public abstract class BaseMockTest {
    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }
}
