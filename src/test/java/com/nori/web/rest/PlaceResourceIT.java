package com.nori.web.rest;

import static com.nori.domain.PlaceAsserts.*;
import static com.nori.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nori.IntegrationTest;
import com.nori.domain.Place;
import com.nori.repository.PlaceRepository;
import com.nori.service.dto.PlaceDTO;
import com.nori.service.mapper.PlaceMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link PlaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaceResourceIT {

    private static final String DEFAULT_PLACE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceMapper placeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlaceMockMvc;

    private Place place;

    private Place insertedPlace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createEntity() {
        return new Place().placeName(DEFAULT_PLACE_NAME).placeType(DEFAULT_PLACE_TYPE).city(DEFAULT_CITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Place createUpdatedEntity() {
        return new Place().placeName(UPDATED_PLACE_NAME).placeType(UPDATED_PLACE_TYPE).city(UPDATED_CITY);
    }

    @BeforeEach
    void initTest() {
        place = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlace != null) {
            placeRepository.delete(insertedPlace);
            insertedPlace = null;
        }
    }

    @Test
    @Transactional
    void createPlace() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);
        var returnedPlaceDTO = om.readValue(
            restPlaceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlaceDTO.class
        );

        // Validate the Place in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlace = placeMapper.toEntity(returnedPlaceDTO);
        assertPlaceUpdatableFieldsEquals(returnedPlace, getPersistedPlace(returnedPlace));

        insertedPlace = returnedPlace;
    }

    @Test
    @Transactional
    void createPlaceWithExistingId() throws Exception {
        // Create the Place with an existing ID
        place.setId(1L);
        PlaceDTO placeDTO = placeMapper.toDto(place);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPlaceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        place.setPlaceName(null);

        // Create the Place, which fails.
        PlaceDTO placeDTO = placeMapper.toDto(place);

        restPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlaces() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        // Get all the placeList
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].placeName").value(hasItem(DEFAULT_PLACE_NAME)))
            .andExpect(jsonPath("$.[*].placeType").value(hasItem(DEFAULT_PLACE_TYPE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    @Transactional
    void getPlace() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc
            .perform(get(ENTITY_API_URL_ID, place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.placeName").value(DEFAULT_PLACE_NAME))
            .andExpect(jsonPath("$.placeType").value(DEFAULT_PLACE_TYPE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    void getNonExistingPlace() throws Exception {
        // Get the place
        restPlaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlace() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place
        Place updatedPlace = placeRepository.findById(place.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlace are not directly saved in db
        em.detach(updatedPlace);
        updatedPlace.placeName(UPDATED_PLACE_NAME).placeType(UPDATED_PLACE_TYPE).city(UPDATED_CITY);
        PlaceDTO placeDTO = placeMapper.toDto(updatedPlace);

        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaceToMatchAllProperties(updatedPlace);
    }

    @Test
    @Transactional
    void putNonExistingPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, placeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(placeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace.placeType(UPDATED_PLACE_TYPE);

        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlace))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPlace, place), getPersistedPlace(place));
    }

    @Test
    @Transactional
    void fullUpdatePlaceWithPatch() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the place using partial update
        Place partialUpdatedPlace = new Place();
        partialUpdatedPlace.setId(place.getId());

        partialUpdatedPlace.placeName(UPDATED_PLACE_NAME).placeType(UPDATED_PLACE_TYPE).city(UPDATED_CITY);

        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlace))
            )
            .andExpect(status().isOk());

        // Validate the Place in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaceUpdatableFieldsEquals(partialUpdatedPlace, getPersistedPlace(partialUpdatedPlace));
    }

    @Test
    @Transactional
    void patchNonExistingPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, placeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(placeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlace() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        place.setId(longCount.incrementAndGet());

        // Create the Place
        PlaceDTO placeDTO = placeMapper.toDto(place);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(placeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Place in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlace() throws Exception {
        // Initialize the database
        insertedPlace = placeRepository.saveAndFlush(place);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the place
        restPlaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, place.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return placeRepository.count();
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

    protected Place getPersistedPlace(Place place) {
        return placeRepository.findById(place.getId()).orElseThrow();
    }

    protected void assertPersistedPlaceToMatchAllProperties(Place expectedPlace) {
        assertPlaceAllPropertiesEquals(expectedPlace, getPersistedPlace(expectedPlace));
    }

    protected void assertPersistedPlaceToMatchUpdatableProperties(Place expectedPlace) {
        assertPlaceAllUpdatablePropertiesEquals(expectedPlace, getPersistedPlace(expectedPlace));
    }
}
