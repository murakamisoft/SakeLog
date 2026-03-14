package com.nori.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DrinkLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DrinkLog getDrinkLogSample1() {
        return new DrinkLog().id(1L).quantity(1).rating(1).memo("memo1");
    }

    public static DrinkLog getDrinkLogSample2() {
        return new DrinkLog().id(2L).quantity(2).rating(2).memo("memo2");
    }

    public static DrinkLog getDrinkLogRandomSampleGenerator() {
        return new DrinkLog()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .rating(intCount.incrementAndGet())
            .memo(UUID.randomUUID().toString());
    }
}
