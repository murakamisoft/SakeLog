package com.nori.service.mapper;

import static com.nori.domain.DrinkLogAsserts.*;
import static com.nori.domain.DrinkLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DrinkLogMapperTest {

    private DrinkLogMapper drinkLogMapper;

    @BeforeEach
    void setUp() {
        drinkLogMapper = new DrinkLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDrinkLogSample1();
        var actual = drinkLogMapper.toEntity(drinkLogMapper.toDto(expected));
        assertDrinkLogAllPropertiesEquals(expected, actual);
    }
}
