package org.sheep1500.toyadvertisementbackend.ads.domain.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.*;

public class JoinLimitFixture {
    public static JoinLimit createJoinLimit(Integer limit) {
        return new JoinLimit(limit);
    }
}
