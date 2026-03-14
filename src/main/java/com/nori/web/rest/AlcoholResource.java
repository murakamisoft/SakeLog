package com.nori.web.rest;

import com.nori.repository.AlcoholRepository;
import com.nori.service.AlcoholService;
import com.nori.service.dto.AlcoholDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.nori.domain.Alcohol}.
 */
@RestController
@RequestMapping("/api/alcohol")
public class AlcoholResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlcoholResource.class);

    private static final String ENTITY_NAME = "alcohol";

    @Value("${jhipster.clientApp.name:sakeLog}")
    private String applicationName;

    private final AlcoholService alcoholService;

    private final AlcoholRepository alcoholRepository;

    public AlcoholResource(AlcoholService alcoholService, AlcoholRepository alcoholRepository) {
        this.alcoholService = alcoholService;
        this.alcoholRepository = alcoholRepository;
    }

    /**
     * {@code POST  /alcohol} : Create a new alcohol.
     *
     * @param alcoholDTO the alcoholDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alcoholDTO, or with status {@code 400 (Bad Request)} if the alcohol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlcoholDTO> createAlcohol(@Valid @RequestBody AlcoholDTO alcoholDTO) throws URISyntaxException {
        LOG.debug("REST request to save Alcohol : {}", alcoholDTO);
        if (alcoholDTO.getId() != null) {
            throw new BadRequestAlertException("A new alcohol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alcoholDTO = alcoholService.save(alcoholDTO);
        return ResponseEntity.created(new URI("/api/alcohol/" + alcoholDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, alcoholDTO.getId().toString()))
            .body(alcoholDTO);
    }

    /**
     * {@code PUT  /alcohol/:id} : Updates an existing alcohol.
     *
     * @param id the id of the alcoholDTO to save.
     * @param alcoholDTO the alcoholDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alcoholDTO,
     * or with status {@code 400 (Bad Request)} if the alcoholDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alcoholDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlcoholDTO> updateAlcohol(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlcoholDTO alcoholDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Alcohol : {}, {}", id, alcoholDTO);
        if (alcoholDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alcoholDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alcoholRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alcoholDTO = alcoholService.update(alcoholDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alcoholDTO.getId().toString()))
            .body(alcoholDTO);
    }

    /**
     * {@code PATCH  /alcohol/:id} : Partial updates given fields of an existing alcohol, field will ignore if it is null
     *
     * @param id the id of the alcoholDTO to save.
     * @param alcoholDTO the alcoholDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alcoholDTO,
     * or with status {@code 400 (Bad Request)} if the alcoholDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alcoholDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alcoholDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlcoholDTO> partialUpdateAlcohol(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlcoholDTO alcoholDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Alcohol partially : {}, {}", id, alcoholDTO);
        if (alcoholDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alcoholDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alcoholRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlcoholDTO> result = alcoholService.partialUpdate(alcoholDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, alcoholDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alcohol} : get all the Alcohol.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Alcohol in body.
     */
    @GetMapping("")
    public List<AlcoholDTO> getAllAlcohols() {
        LOG.debug("REST request to get all Alcohols");
        return alcoholService.findAll();
    }

    /**
     * {@code GET  /alcohol/:id} : get the "id" alcohol.
     *
     * @param id the id of the alcoholDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alcoholDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlcoholDTO> getAlcohol(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Alcohol : {}", id);
        Optional<AlcoholDTO> alcoholDTO = alcoholService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alcoholDTO);
    }

    /**
     * {@code DELETE  /alcohol/:id} : delete the "id" alcohol.
     *
     * @param id the id of the alcoholDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlcohol(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Alcohol : {}", id);
        alcoholService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
