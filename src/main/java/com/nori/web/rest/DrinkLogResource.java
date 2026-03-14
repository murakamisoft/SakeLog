package com.nori.web.rest;

import com.nori.repository.DrinkLogRepository;
import com.nori.service.DrinkLogService;
import com.nori.service.dto.DrinkLogDTO;
import com.nori.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nori.domain.DrinkLog}.
 */
@RestController
@RequestMapping("/api/drink-logs")
public class DrinkLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(DrinkLogResource.class);

    private static final String ENTITY_NAME = "drinkLog";

    @Value("${jhipster.clientApp.name:sakeLog}")
    private String applicationName;

    private final DrinkLogService drinkLogService;

    private final DrinkLogRepository drinkLogRepository;

    public DrinkLogResource(DrinkLogService drinkLogService, DrinkLogRepository drinkLogRepository) {
        this.drinkLogService = drinkLogService;
        this.drinkLogRepository = drinkLogRepository;
    }

    /**
     * {@code POST  /drink-logs} : Create a new drinkLog.
     *
     * @param drinkLogDTO the drinkLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new drinkLogDTO, or with status {@code 400 (Bad Request)} if the drinkLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DrinkLogDTO> createDrinkLog(@Valid @RequestBody DrinkLogDTO drinkLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save DrinkLog : {}", drinkLogDTO);
        if (drinkLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new drinkLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        drinkLogDTO = drinkLogService.save(drinkLogDTO);
        return ResponseEntity.created(new URI("/api/drink-logs/" + drinkLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, drinkLogDTO.getId().toString()))
            .body(drinkLogDTO);
    }

    /**
     * {@code PUT  /drink-logs/:id} : Updates an existing drinkLog.
     *
     * @param id the id of the drinkLogDTO to save.
     * @param drinkLogDTO the drinkLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drinkLogDTO,
     * or with status {@code 400 (Bad Request)} if the drinkLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the drinkLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DrinkLogDTO> updateDrinkLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DrinkLogDTO drinkLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DrinkLog : {}, {}", id, drinkLogDTO);
        if (drinkLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, drinkLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!drinkLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        drinkLogDTO = drinkLogService.update(drinkLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, drinkLogDTO.getId().toString()))
            .body(drinkLogDTO);
    }

    /**
     * {@code PATCH  /drink-logs/:id} : Partial updates given fields of an existing drinkLog, field will ignore if it is null
     *
     * @param id the id of the drinkLogDTO to save.
     * @param drinkLogDTO the drinkLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated drinkLogDTO,
     * or with status {@code 400 (Bad Request)} if the drinkLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the drinkLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the drinkLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DrinkLogDTO> partialUpdateDrinkLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DrinkLogDTO drinkLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DrinkLog partially : {}, {}", id, drinkLogDTO);
        if (drinkLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, drinkLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!drinkLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DrinkLogDTO> result = drinkLogService.partialUpdate(drinkLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, drinkLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /drink-logs} : get all the Drink Logs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Drink Logs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DrinkLogDTO>> getAllDrinkLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DrinkLogs");
        Page<DrinkLogDTO> page = drinkLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /drink-logs/:id} : get the "id" drinkLog.
     *
     * @param id the id of the drinkLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the drinkLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DrinkLogDTO> getDrinkLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DrinkLog : {}", id);
        Optional<DrinkLogDTO> drinkLogDTO = drinkLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(drinkLogDTO);
    }

    /**
     * {@code DELETE  /drink-logs/:id} : delete the "id" drinkLog.
     *
     * @param id the id of the drinkLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrinkLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DrinkLog : {}", id);
        drinkLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
