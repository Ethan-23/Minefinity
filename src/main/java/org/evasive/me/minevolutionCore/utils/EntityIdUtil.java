package org.evasive.me.minevolutionCore.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class EntityIdUtil {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(2_000_000);

    public static int getNextEntityId() {
        return NEXT_ID.getAndIncrement();
    }
}
