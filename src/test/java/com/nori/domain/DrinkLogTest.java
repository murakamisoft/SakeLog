package com.nori.domain;

import static com.nori.domain.AlcoholTestSamples.*;
import static com.nori.domain.DrinkLogTestSamples.*;
import static com.nori.domain.PlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nori.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DrinkLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrinkLog.class);
        DrinkLog drinkLog1 = getDrinkLogSample1();
        DrinkLog drinkLog2 = new DrinkLog();
        assertThat(drinkLog1).isNotEqualTo(drinkLog2);

        drinkLog2.setId(drinkLog1.getId());
        assertThat(drinkLog1).isEqualTo(drinkLog2);

        drinkLog2 = getDrinkLogSample2();
        assertThat(drinkLog1).isNotEqualTo(drinkLog2);
    }

    @Test
    void alcoholTest() {
        DrinkLog drinkLog = getDrinkLogRandomSampleGenerator();
        Alcohol alcoholBack = getAlcoholRandomSampleGenerator();

        drinkLog.setAlcohol(alcoholBack);
        assertThat(drinkLog.getAlcohol()).isEqualTo(alcoholBack);

        drinkLog.alcohol(null);
        assertThat(drinkLog.getAlcohol()).isNull();
    }

    @Test
    void placeTest() {
        DrinkLog drinkLog = getDrinkLogRandomSampleGenerator();
        Place placeBack = getPlaceRandomSampleGenerator();

        drinkLog.setPlace(placeBack);
        assertThat(drinkLog.getPlace()).isEqualTo(placeBack);

        drinkLog.place(null);
        assertThat(drinkLog.getPlace()).isNull();
    }
}
