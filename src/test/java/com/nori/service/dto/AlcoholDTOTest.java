package com.nori.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.nori.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlcoholDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlcoholDTO.class);
        AlcoholDTO alcoholDTO1 = new AlcoholDTO();
        alcoholDTO1.setId(1L);
        AlcoholDTO alcoholDTO2 = new AlcoholDTO();
        assertThat(alcoholDTO1).isNotEqualTo(alcoholDTO2);
        alcoholDTO2.setId(alcoholDTO1.getId());
        assertThat(alcoholDTO1).isEqualTo(alcoholDTO2);
        alcoholDTO2.setId(2L);
        assertThat(alcoholDTO1).isNotEqualTo(alcoholDTO2);
        alcoholDTO1.setId(null);
        assertThat(alcoholDTO1).isNotEqualTo(alcoholDTO2);
    }
}
