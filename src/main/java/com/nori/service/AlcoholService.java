package com.nori.service;

import com.nori.domain.Alcohol;
import com.nori.repository.AlcoholRepository;
import com.nori.service.dto.AlcoholDTO;
import com.nori.service.mapper.AlcoholMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nori.domain.Alcohol}.
 */
@Service
@Transactional
public class AlcoholService {

    private static final Logger LOG = LoggerFactory.getLogger(AlcoholService.class);

    private final AlcoholRepository alcoholRepository;

    private final AlcoholMapper alcoholMapper;

    public AlcoholService(AlcoholRepository alcoholRepository, AlcoholMapper alcoholMapper) {
        this.alcoholRepository = alcoholRepository;
        this.alcoholMapper = alcoholMapper;
    }

    /**
     * Save a alcohol.
     *
     * @param alcoholDTO the entity to save.
     * @return the persisted entity.
     */
    public AlcoholDTO save(AlcoholDTO alcoholDTO) {
        LOG.debug("Request to save Alcohol : {}", alcoholDTO);
        Alcohol alcohol = alcoholMapper.toEntity(alcoholDTO);
        alcohol = alcoholRepository.save(alcohol);
        return alcoholMapper.toDto(alcohol);
    }

    /**
     * Update a alcohol.
     *
     * @param alcoholDTO the entity to save.
     * @return the persisted entity.
     */
    public AlcoholDTO update(AlcoholDTO alcoholDTO) {
        LOG.debug("Request to update Alcohol : {}", alcoholDTO);
        Alcohol alcohol = alcoholMapper.toEntity(alcoholDTO);
        alcohol = alcoholRepository.save(alcohol);
        return alcoholMapper.toDto(alcohol);
    }

    /**
     * Partially update a alcohol.
     *
     * @param alcoholDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlcoholDTO> partialUpdate(AlcoholDTO alcoholDTO) {
        LOG.debug("Request to partially update Alcohol : {}", alcoholDTO);

        return alcoholRepository
            .findById(alcoholDTO.getId())
            .map(existingAlcohol -> {
                alcoholMapper.partialUpdate(existingAlcohol, alcoholDTO);

                return existingAlcohol;
            })
            .map(alcoholRepository::save)
            .map(alcoholMapper::toDto);
    }

    /**
     * Get all the alcohols.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AlcoholDTO> findAll() {
        LOG.debug("Request to get all Alcohols");
        return alcoholRepository.findAll().stream().map(alcoholMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one alcohol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlcoholDTO> findOne(Long id) {
        LOG.debug("Request to get Alcohol : {}", id);
        return alcoholRepository.findById(id).map(alcoholMapper::toDto);
    }

    /**
     * Delete the alcohol by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Alcohol : {}", id);
        alcoholRepository.deleteById(id);
    }
}
