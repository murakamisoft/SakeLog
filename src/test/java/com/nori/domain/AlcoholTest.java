package com.nori.domain;

import static com.nori.domain.AlcoholTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nori.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlcoholTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Alcohol.class);
        Alcohol alcohol1 = getAlcoholSample1();
        Alcohol alcohol2 = new Alcohol();
        assertThat(alcohol1).isNotEqualTo(alcohol2);

        alcohol2.setId(alcohol1.getId());
        assertThat(alcohol1).isEqualTo(alcohol2);

        alcohol2 = getAlcoholSample2();
        assertThat(alcohol1).isNotEqualTo(alcohol2);
    }
}
