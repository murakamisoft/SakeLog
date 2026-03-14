package com.nori.domain;

import static com.nori.domain.PlaceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nori.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Place.class);
        Place place1 = getPlaceSample1();
        Place place2 = new Place();
        assertThat(place1).isNotEqualTo(place2);

        place2.setId(place1.getId());
        assertThat(place1).isEqualTo(place2);

        place2 = getPlaceSample2();
        assertThat(place1).isNotEqualTo(place2);
    }
}
