package com.nori.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nori.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DrinkLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrinkLogDTO.class);
        DrinkLogDTO drinkLogDTO1 = new DrinkLogDTO();
        drinkLogDTO1.setId(1L);
        DrinkLogDTO drinkLogDTO2 = new DrinkLogDTO();
        assertThat(drinkLogDTO1).isNotEqualTo(drinkLogDTO2);
        drinkLogDTO2.setId(drinkLogDTO1.getId());
        assertThat(drinkLogDTO1).isEqualTo(drinkLogDTO2);
        drinkLogDTO2.setId(2L);
        assertThat(drinkLogDTO1).isNotEqualTo(drinkLogDTO2);
        drinkLogDTO1.setId(null);
        assertThat(drinkLogDTO1).isNotEqualTo(drinkLogDTO2);
    }
}
