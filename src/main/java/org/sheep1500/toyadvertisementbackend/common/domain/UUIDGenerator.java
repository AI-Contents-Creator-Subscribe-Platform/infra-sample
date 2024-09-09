package org.sheep1500.toyadvertisementbackend.common.domain;

import java.util.UUID;

public class UUIDGenerator {

    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }
}
