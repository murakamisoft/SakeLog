package com.nori.service;

import com.nori.domain.DrinkLog;
import com.nori.repository.DrinkLogRepository;
import com.nori.service.dto.DrinkLogDTO;
import com.nori.service.mapper.DrinkLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.nori.domain.DrinkLog}.
 */
@Service
@Transactional
public class DrinkLogService {

    private static final Logger LOG = LoggerFactory.getLogger(DrinkLogService.class);

    private final DrinkLogRepository drinkLogRepository;

    private final DrinkLogMapper drinkLogMapper;

    public DrinkLogService(DrinkLogRepository drinkLogRepository, DrinkLogMapper drinkLogMapper) {
        this.drinkLogRepository = drinkLogRepository;
        this.drinkLogMapper = drinkLogMapper;
    }

    /**
     * Save a drinkLog.
     *
     * @param drinkLogDTO the entity to save.
     * @return the persisted entity.
     */
    public DrinkLogDTO save(DrinkLogDTO drinkLogDTO) {
        LOG.debug("Request to save DrinkLog : {}", drinkLogDTO);
        DrinkLog drinkLog = drinkLogMapper.toEntity(drinkLogDTO);
        drinkLog = drinkLogRepository.save(drinkLog);
        return drinkLogMapper.toDto(drinkLog);
    }

    /**
     * Update a drinkLog.
     *
     * @param drinkLogDTO the entity to save.
     * @return the persisted entity.
     */
    public DrinkLogDTO update(DrinkLogDTO drinkLogDTO) {
        LOG.debug("Request to update DrinkLog : {}", drinkLogDTO);
        DrinkLog drinkLog = drinkLogMapper.toEntity(drinkLogDTO);
        drinkLog = drinkLogRepository.save(drinkLog);
        return drinkLogMapper.toDto(drinkLog);
    }

    /**
     * Partially update a drinkLog.
     *
     * @param drinkLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DrinkLogDTO> partialUpdate(DrinkLogDTO drinkLogDTO) {
        LOG.debug("Request to partially update DrinkLog : {}", drinkLogDTO);

        return drinkLogRepository
            .findById(drinkLogDTO.getId())
            .map(existingDrinkLog -> {
                drinkLogMapper.partialUpdate(existingDrinkLog, drinkLogDTO);

                return existingDrinkLog;
            })
            .map(drinkLogRepository::save)
            .map(drinkLogMapper::toDto);
    }

    /**
     * Get all the drinkLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DrinkLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DrinkLogs");
        return drinkLogRepository.findAll(pageable).map(drinkLogMapper::toDto);
    }

    /**
     * Get one drinkLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DrinkLogDTO> findOne(Long id) {
        LOG.debug("Request to get DrinkLog : {}", id);
        return drinkLogRepository.findById(id).map(drinkLogMapper::toDto);
    }

    /**
     * Delete the drinkLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DrinkLog : {}", id);
        drinkLogRepository.deleteById(id);
    }
}
