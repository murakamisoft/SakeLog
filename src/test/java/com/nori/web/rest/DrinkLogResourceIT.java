package com.nori.web.rest;

import static com.nori.domain.DrinkLogAsserts.*;
import static com.nori.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nori.IntegrationTest;
import com.nori.domain.DrinkLog;
import com.nori.repository.DrinkLogRepository;
import com.nori.repository.UserRepository;
import com.nori.service.dto.DrinkLogDTO;
import com.nori.service.mapper.DrinkLogMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DrinkLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DrinkLogResourceIT {

    private static final Instant DEFAULT_DRINK_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DRINK_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String DEFAULT_MEMO = "AAAAAAAAAA";
    private static final String UPDATED_MEMO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/drink-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DrinkLogRepository drinkLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DrinkLogMapper drinkLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDrinkLogMockMvc;

    private DrinkLog drinkLog;

    private DrinkLog insertedDrinkLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DrinkLog createEntity() {
        return new DrinkLog()
            .drinkDate(DEFAULT_DRINK_DATE)
            .quantity(DEFAULT_QUANTITY)
            .rating(DEFAULT_RATING)
            .memo(DEFAULT_MEMO)
            .createdAt(DEFAULT_CREATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DrinkLog createUpdatedEntity() {
        return new DrinkLog()
            .drinkDate(UPDATED_DRINK_DATE)
            .quantity(UPDATED_QUANTITY)
            .rating(UPDATED_RATING)
            .memo(UPDATED_MEMO)
            .createdAt(UPDATED_CREATED_AT);
    }

    @BeforeEach
    void initTest() {
        drinkLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDrinkLog != null) {
            drinkLogRepository.delete(insertedDrinkLog);
            insertedDrinkLog = null;
        }
    }

    @Test
    @Transactional
    void createDrinkLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);
        var returnedDrinkLogDTO = om.readValue(
            restDrinkLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(drinkLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DrinkLogDTO.class
        );

        // Validate the DrinkLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDrinkLog = drinkLogMapper.toEntity(returnedDrinkLogDTO);
        assertDrinkLogUpdatableFieldsEquals(returnedDrinkLog, getPersistedDrinkLog(returnedDrinkLog));

        insertedDrinkLog = returnedDrinkLog;
    }

    @Test
    @Transactional
    void createDrinkLogWithExistingId() throws Exception {
        // Create the DrinkLog with an existing ID
        drinkLog.setId(1L);
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDrinkLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(drinkLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDrinkDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        drinkLog.setDrinkDate(null);

        // Create the DrinkLog, which fails.
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        restDrinkLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(drinkLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDrinkLogs() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        // Get all the drinkLogList
        restDrinkLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drinkLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].drinkDate").value(hasItem(DEFAULT_DRINK_DATE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].memo").value(hasItem(DEFAULT_MEMO)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getDrinkLog() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        // Get the drinkLog
        restDrinkLogMockMvc
            .perform(get(ENTITY_API_URL_ID, drinkLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(drinkLog.getId().intValue()))
            .andExpect(jsonPath("$.drinkDate").value(DEFAULT_DRINK_DATE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.memo").value(DEFAULT_MEMO))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDrinkLog() throws Exception {
        // Get the drinkLog
        restDrinkLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDrinkLog() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the drinkLog
        DrinkLog updatedDrinkLog = drinkLogRepository.findById(drinkLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDrinkLog are not directly saved in db
        em.detach(updatedDrinkLog);
        updatedDrinkLog
            .drinkDate(UPDATED_DRINK_DATE)
            .quantity(UPDATED_QUANTITY)
            .rating(UPDATED_RATING)
            .memo(UPDATED_MEMO)
            .createdAt(UPDATED_CREATED_AT);
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(updatedDrinkLog);

        restDrinkLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, drinkLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(drinkLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDrinkLogToMatchAllProperties(updatedDrinkLog);
    }

    @Test
    @Transactional
    void putNonExistingDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, drinkLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(drinkLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(drinkLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(drinkLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDrinkLogWithPatch() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the drinkLog using partial update
        DrinkLog partialUpdatedDrinkLog = new DrinkLog();
        partialUpdatedDrinkLog.setId(drinkLog.getId());

        partialUpdatedDrinkLog.drinkDate(UPDATED_DRINK_DATE).rating(UPDATED_RATING).memo(UPDATED_MEMO);

        restDrinkLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrinkLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDrinkLog))
            )
            .andExpect(status().isOk());

        // Validate the DrinkLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDrinkLogUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDrinkLog, drinkLog), getPersistedDrinkLog(drinkLog));
    }

    @Test
    @Transactional
    void fullUpdateDrinkLogWithPatch() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the drinkLog using partial update
        DrinkLog partialUpdatedDrinkLog = new DrinkLog();
        partialUpdatedDrinkLog.setId(drinkLog.getId());

        partialUpdatedDrinkLog
            .drinkDate(UPDATED_DRINK_DATE)
            .quantity(UPDATED_QUANTITY)
            .rating(UPDATED_RATING)
            .memo(UPDATED_MEMO)
            .createdAt(UPDATED_CREATED_AT);

        restDrinkLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDrinkLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDrinkLog))
            )
            .andExpect(status().isOk());

        // Validate the DrinkLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDrinkLogUpdatableFieldsEquals(partialUpdatedDrinkLog, getPersistedDrinkLog(partialUpdatedDrinkLog));
    }

    @Test
    @Transactional
    void patchNonExistingDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, drinkLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(drinkLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(drinkLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDrinkLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        drinkLog.setId(longCount.incrementAndGet());

        // Create the DrinkLog
        DrinkLogDTO drinkLogDTO = drinkLogMapper.toDto(drinkLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDrinkLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(drinkLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DrinkLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDrinkLog() throws Exception {
        // Initialize the database
        insertedDrinkLog = drinkLogRepository.saveAndFlush(drinkLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the drinkLog
        restDrinkLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, drinkLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return drinkLogRepository.count();
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

    protected DrinkLog getPersistedDrinkLog(DrinkLog drinkLog) {
        return drinkLogRepository.findById(drinkLog.getId()).orElseThrow();
    }

    protected void assertPersistedDrinkLogToMatchAllProperties(DrinkLog expectedDrinkLog) {
        assertDrinkLogAllPropertiesEquals(expectedDrinkLog, getPersistedDrinkLog(expectedDrinkLog));
    }

    protected void assertPersistedDrinkLogToMatchUpdatableProperties(DrinkLog expectedDrinkLog) {
        assertDrinkLogAllUpdatablePropertiesEquals(expectedDrinkLog, getPersistedDrinkLog(expectedDrinkLog));
    }
}
