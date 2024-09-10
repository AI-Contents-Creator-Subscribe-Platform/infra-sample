package org.sheep1500.toyadvertisementbackend.common.domain;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

public class IdGenerator {
    private static String simpleTimestamp() { // len : 23 (10+13)
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String today = sdf.format(new java.util.Date());
        return today;
    }

    public static String simpleTimestampUuid() { // len : 23 (10+13)
        String id = simpleTimestamp() + shortUuid();
        return id;
    }
    private static String shortUuid() { // len : 13
        UUID uuid = UUID.randomUUID();
        String id = Long.toString(
                uuid.getMostSignificantBits(),
                Character.MAX_RADIX
        ).replace("-", "");

        int limit = 13;
        if (id.length() > limit) {
            id = id.substring(0, limit);
        } else {
            while (id.length() < limit) {
                id += new Random().nextInt(9);
            }
        }

        return id;
    }
}
