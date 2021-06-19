package br.com.trabalho.web.rest;

import br.com.trabalho.domain.Assunto;
import br.com.trabalho.repository.AssuntoRepository;
import br.com.trabalho.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.trabalho.domain.Assunto}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AssuntoResource {

    private final Logger log = LoggerFactory.getLogger(AssuntoResource.class);

    private static final String ENTITY_NAME = "assunto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssuntoRepository assuntoRepository;

    public AssuntoResource(AssuntoRepository assuntoRepository) {
        this.assuntoRepository = assuntoRepository;
    }

    /**
     * {@code POST  /assuntos} : Create a new assunto.
     *
     * @param assunto the assunto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assunto, or with status {@code 400 (Bad Request)} if the assunto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/assuntos")
    public ResponseEntity<Assunto> createAssunto(@Valid @RequestBody Assunto assunto) throws URISyntaxException {
        log.debug("REST request to save Assunto : {}", assunto);
        if (assunto.getId() != null) {
            throw new BadRequestAlertException("A new assunto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assunto result = assuntoRepository.save(assunto);
        return ResponseEntity
            .created(new URI("/api/assuntos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /assuntos/:id} : Updates an existing assunto.
     *
     * @param id the id of the assunto to save.
     * @param assunto the assunto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assunto,
     * or with status {@code 400 (Bad Request)} if the assunto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assunto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/assuntos/{id}")
    public ResponseEntity<Assunto> updateAssunto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Assunto assunto
    ) throws URISyntaxException {
        log.debug("REST request to update Assunto : {}, {}", id, assunto);
        if (assunto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assunto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuntoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Assunto result = assuntoRepository.save(assunto);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assunto.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /assuntos/:id} : Partial updates given fields of an existing assunto, field will ignore if it is null
     *
     * @param id the id of the assunto to save.
     * @param assunto the assunto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assunto,
     * or with status {@code 400 (Bad Request)} if the assunto is not valid,
     * or with status {@code 404 (Not Found)} if the assunto is not found,
     * or with status {@code 500 (Internal Server Error)} if the assunto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/assuntos/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Assunto> partialUpdateAssunto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Assunto assunto
    ) throws URISyntaxException {
        log.debug("REST request to partial update Assunto partially : {}, {}", id, assunto);
        if (assunto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assunto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assuntoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Assunto> result = assuntoRepository
            .findById(assunto.getId())
            .map(
                existingAssunto -> {
                    if (assunto.getCodigoassunto() != null) {
                        existingAssunto.setCodigoassunto(assunto.getCodigoassunto());
                    }
                    if (assunto.getNome() != null) {
                        existingAssunto.setNome(assunto.getNome());
                    }
                    if (assunto.getCarga() != null) {
                        existingAssunto.setCarga(assunto.getCarga());
                    }
                    if (assunto.getIdprofessor() != null) {
                        existingAssunto.setIdprofessor(assunto.getIdprofessor());
                    }

                    return existingAssunto;
                }
            )
            .map(assuntoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assunto.getId().toString())
        );
    }

    /**
     * {@code GET  /assuntos} : get all the assuntos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assuntos in body.
     */
    @GetMapping("/assuntos")
    public List<Assunto> getAllAssuntos() {
        log.debug("REST request to get all Assuntos");
        return assuntoRepository.findAll();
    }

    /**
     * {@code GET  /assuntos/:id} : get the "id" assunto.
     *
     * @param id the id of the assunto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assunto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/assuntos/{id}")
    public ResponseEntity<Assunto> getAssunto(@PathVariable Long id) {
        log.debug("REST request to get Assunto : {}", id);
        Optional<Assunto> assunto = assuntoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assunto);
    }

    /**
     * {@code DELETE  /assuntos/:id} : delete the "id" assunto.
     *
     * @param id the id of the assunto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/assuntos/{id}")
    public ResponseEntity<Void> deleteAssunto(@PathVariable Long id) {
        log.debug("REST request to delete Assunto : {}", id);
        assuntoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
