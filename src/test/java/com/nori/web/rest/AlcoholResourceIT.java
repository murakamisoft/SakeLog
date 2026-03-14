package com.nori.web.rest;

import static com.nori.domain.AlcoholAsserts.*;
import static com.nori.web.rest.TestUtil.createUpdateProxyForBean;
import static com.nori.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nori.IntegrationTest;
import com.nori.domain.Alcohol;
import com.nori.repository.AlcoholRepository;
import com.nori.service.dto.AlcoholDTO;
import com.nori.service.mapper.AlcoholMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlcoholResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlcoholResourceIT {

    private static final String DEFAULT_ALCOHOL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ALCOHOL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ALCOHOL_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ALCOHOL_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ABV = new BigDecimal(1);
    private static final BigDecimal UPDATED_ABV = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/alcohol";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlcoholRepository alcoholRepository;

    @Autowired
    private AlcoholMapper alcoholMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlcoholMockMvc;

    private Alcohol alcohol;

    private Alcohol insertedAlcohol;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alcohol createEntity() {
        return new Alcohol()
            .alcoholName(DEFAULT_ALCOHOL_NAME)
            .alcoholType(DEFAULT_ALCOHOL_TYPE)
            .brandName(DEFAULT_BRAND_NAME)
            .abv(DEFAULT_ABV);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alcohol createUpdatedEntity() {
        return new Alcohol()
            .alcoholName(UPDATED_ALCOHOL_NAME)
            .alcoholType(UPDATED_ALCOHOL_TYPE)
            .brandName(UPDATED_BRAND_NAME)
            .abv(UPDATED_ABV);
    }

    @BeforeEach
    void initTest() {
        alcohol = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlcohol != null) {
            alcoholRepository.delete(insertedAlcohol);
            insertedAlcohol = null;
        }
    }

    @Test
    @Transactional
    void createAlcohol() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);
        var returnedAlcoholDTO = om.readValue(
            restAlcoholMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlcoholDTO.class
        );

        // Validate the Alcohol in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlcohol = alcoholMapper.toEntity(returnedAlcoholDTO);
        assertAlcoholUpdatableFieldsEquals(returnedAlcohol, getPersistedAlcohol(returnedAlcohol));

        insertedAlcohol = returnedAlcohol;
    }

    @Test
    @Transactional
    void createAlcoholWithExistingId() throws Exception {
        // Create the Alcohol with an existing ID
        alcohol.setId(1L);
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlcoholMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAlcoholNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alcohol.setAlcoholName(null);

        // Create the Alcohol, which fails.
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        restAlcoholMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlcohols() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        // Get all the alcoholList
        restAlcoholMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alcohol.getId().intValue())))
            .andExpect(jsonPath("$.[*].alcoholName").value(hasItem(DEFAULT_ALCOHOL_NAME)))
            .andExpect(jsonPath("$.[*].alcoholType").value(hasItem(DEFAULT_ALCOHOL_TYPE)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].abv").value(hasItem(sameNumber(DEFAULT_ABV))));
    }

    @Test
    @Transactional
    void getAlcohol() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        // Get the alcohol
        restAlcoholMockMvc
            .perform(get(ENTITY_API_URL_ID, alcohol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alcohol.getId().intValue()))
            .andExpect(jsonPath("$.alcoholName").value(DEFAULT_ALCOHOL_NAME))
            .andExpect(jsonPath("$.alcoholType").value(DEFAULT_ALCOHOL_TYPE))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.abv").value(sameNumber(DEFAULT_ABV)));
    }

    @Test
    @Transactional
    void getNonExistingAlcohol() throws Exception {
        // Get the alcohol
        restAlcoholMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlcohol() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alcohol
        Alcohol updatedAlcohol = alcoholRepository.findById(alcohol.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlcohol are not directly saved in db
        em.detach(updatedAlcohol);
        updatedAlcohol.alcoholName(UPDATED_ALCOHOL_NAME).alcoholType(UPDATED_ALCOHOL_TYPE).brandName(UPDATED_BRAND_NAME).abv(UPDATED_ABV);
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(updatedAlcohol);

        restAlcoholMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alcoholDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlcoholToMatchAllProperties(updatedAlcohol);
    }

    @Test
    @Transactional
    void putNonExistingAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alcoholDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alcoholDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alcoholDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlcoholWithPatch() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alcohol using partial update
        Alcohol partialUpdatedAlcohol = new Alcohol();
        partialUpdatedAlcohol.setId(alcohol.getId());

        partialUpdatedAlcohol.brandName(UPDATED_BRAND_NAME);

        restAlcoholMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlcohol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlcohol))
            )
            .andExpect(status().isOk());

        // Validate the Alcohol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlcoholUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlcohol, alcohol), getPersistedAlcohol(alcohol));
    }

    @Test
    @Transactional
    void fullUpdateAlcoholWithPatch() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alcohol using partial update
        Alcohol partialUpdatedAlcohol = new Alcohol();
        partialUpdatedAlcohol.setId(alcohol.getId());

        partialUpdatedAlcohol
            .alcoholName(UPDATED_ALCOHOL_NAME)
            .alcoholType(UPDATED_ALCOHOL_TYPE)
            .brandName(UPDATED_BRAND_NAME)
            .abv(UPDATED_ABV);

        restAlcoholMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlcohol.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlcohol))
            )
            .andExpect(status().isOk());

        // Validate the Alcohol in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlcoholUpdatableFieldsEquals(partialUpdatedAlcohol, getPersistedAlcohol(partialUpdatedAlcohol));
    }

    @Test
    @Transactional
    void patchNonExistingAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alcoholDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alcoholDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alcoholDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlcohol() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alcohol.setId(longCount.incrementAndGet());

        // Create the Alcohol
        AlcoholDTO alcoholDTO = alcoholMapper.toDto(alcohol);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlcoholMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alcoholDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alcohol in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlcohol() throws Exception {
        // Initialize the database
        insertedAlcohol = alcoholRepository.saveAndFlush(alcohol);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alcohol
        restAlcoholMockMvc
            .perform(delete(ENTITY_API_URL_ID, alcohol.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alcoholRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Alcohol getPersistedAlcohol(Alcohol alcohol) {
        return alcoholRepository.findById(alcohol.getId()).orElseThrow();
    }

    protected void assertPersistedAlcoholToMatchAllProperties(Alcohol expectedAlcohol) {
        assertAlcoholAllPropertiesEquals(expectedAlcohol, getPersistedAlcohol(expectedAlcohol));
    }

    protected void assertPersistedAlcoholToMatchUpdatableProperties(Alcohol expectedAlcohol) {
        assertAlcoholAllUpdatablePropertiesEquals(expectedAlcohol, getPersistedAlcohol(expectedAlcohol));
    }
}
