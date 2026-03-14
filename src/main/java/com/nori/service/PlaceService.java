package com.nori.service;

import com.nori.domain.Place;
import com.nori.repository.PlaceRepository;
import com.nori.service.dto.PlaceDTO;
import com.nori.service.mapper.PlaceMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nori.domain.Place}.
 */
@Service
@Transactional
public class PlaceService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaceService.class);

    private final PlaceRepository placeRepository;

    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    /**
     * Save a place.
     *
     * @param placeDTO the entity to save.
     * @return the persisted entity.
     */
    public PlaceDTO save(PlaceDTO placeDTO) {
        LOG.debug("Request to save Place : {}", placeDTO);
        Place place = placeMapper.toEntity(placeDTO);
        place = placeRepository.save(place);
        return placeMapper.toDto(place);
    }

    /**
     * Update a place.
     *
     * @param placeDTO the entity to save.
     * @return the persisted entity.
     */
    public PlaceDTO update(PlaceDTO placeDTO) {
        LOG.debug("Request to update Place : {}", placeDTO);
        Place place = placeMapper.toEntity(placeDTO);
        place = placeRepository.save(place);
        return placeMapper.toDto(place);
    }

    /**
     * Partially update a place.
     *
     * @param placeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlaceDTO> partialUpdate(PlaceDTO placeDTO) {
        LOG.debug("Request to partially update Place : {}", placeDTO);

        return placeRepository
            .findById(placeDTO.getId())
            .map(existingPlace -> {
                placeMapper.partialUpdate(existingPlace, placeDTO);

                return existingPlace;
            })
            .map(placeRepository::save)
            .map(placeMapper::toDto);
    }

    /**
     * Get all the places.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlaceDTO> findAll() {
        LOG.debug("Request to get all Places");
        return placeRepository.findAll().stream().map(placeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one place by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlaceDTO> findOne(Long id) {
        LOG.debug("Request to get Place : {}", id);
        return placeRepository.findById(id).map(placeMapper::toDto);
    }

    /**
     * Delete the place by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Place : {}", id);
        placeRepository.deleteById(id);
    }
}
