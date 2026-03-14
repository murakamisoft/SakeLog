package com.nori.service.mapper;

import static com.nori.domain.AlcoholAsserts.*;
import static com.nori.domain.AlcoholTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlcoholMapperTest {

    private AlcoholMapper alcoholMapper;

    @BeforeEach
    void setUp() {
        alcoholMapper = new AlcoholMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlcoholSample1();
        var actual = alcoholMapper.toEntity(alcoholMapper.toDto(expected));
        assertAlcoholAllPropertiesEquals(expected, actual);
    }
}
