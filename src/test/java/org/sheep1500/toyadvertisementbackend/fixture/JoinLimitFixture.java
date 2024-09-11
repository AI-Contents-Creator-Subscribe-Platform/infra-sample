package org.sheep1500.toyadvertisementbackend.fixture;

import org.sheep1500.toyadvertisementbackend.ads.domain.*;

public class JoinLimitFixture {
    public static JoinLimit createJoinLimit(Integer limit) {
        return new JoinLimit(limit);
    }
}
