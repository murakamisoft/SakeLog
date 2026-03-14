package com.nori.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AlcoholTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Alcohol getAlcoholSample1() {
        return new Alcohol().id(1L).alcoholName("alcoholName1").alcoholType("alcoholType1").brandName("brandName1");
    }

    public static Alcohol getAlcoholSample2() {
        return new Alcohol().id(2L).alcoholName("alcoholName2").alcoholType("alcoholType2").brandName("brandName2");
    }

    public static Alcohol getAlcoholRandomSampleGenerator() {
        return new Alcohol()
            .id(longCount.incrementAndGet())
            .alcoholName(UUID.randomUUID().toString())
            .alcoholType(UUID.randomUUID().toString())
            .brandName(UUID.randomUUID().toString());
    }
}
