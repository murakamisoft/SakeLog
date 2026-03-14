package com.nori.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlaceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    public static Place getPlaceSample1() {
        return new Place().id(1L).placeName("placeName1").placeType("placeType1").city("city1");
    }

    public static Place getPlaceSample2() {
        return new Place().id(2L).placeName("placeName2").placeType("placeType2").city("city2");
    }

    public static Place getPlaceRandomSampleGenerator() {
        return new Place()
            .id(longCount.incrementAndGet())
            .placeName(UUID.randomUUID().toString())
            .placeType(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
