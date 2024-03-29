package br.com.trabalho.web.rest;

import static br.com.trabalho.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.trabalho.IntegrationTest;
import br.com.trabalho.domain.Turma;
import br.com.trabalho.repository.TurmaRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TurmaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TurmaResourceIT {

    private static final Integer DEFAULT_CODIGO_TURMA = 1;
    private static final Integer UPDATED_CODIGO_TURMA = 2;

    private static final Integer DEFAULT_SALA = 1;
    private static final Integer UPDATED_SALA = 2;

    private static final ZonedDateTime DEFAULT_ANO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ANO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/turmas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTurmaMockMvc;

    private Turma turma;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createEntity(EntityManager em) {
        Turma turma = new Turma().codigoTurma(DEFAULT_CODIGO_TURMA).sala(DEFAULT_SALA).ano(DEFAULT_ANO);
        return turma;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Turma createUpdatedEntity(EntityManager em) {
        Turma turma = new Turma().codigoTurma(UPDATED_CODIGO_TURMA).sala(UPDATED_SALA).ano(UPDATED_ANO);
        return turma;
    }

    @BeforeEach
    public void initTest() {
        turma = createEntity(em);
    }

    @Test
    @Transactional
    void createTurma() throws Exception {
        int databaseSizeBeforeCreate = turmaRepository.findAll().size();
        // Create the Turma
        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isCreated());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate + 1);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getCodigoTurma()).isEqualTo(DEFAULT_CODIGO_TURMA);
        assertThat(testTurma.getSala()).isEqualTo(DEFAULT_SALA);
        assertThat(testTurma.getAno()).isEqualTo(DEFAULT_ANO);
    }

    @Test
    @Transactional
    void createTurmaWithExistingId() throws Exception {
        // Create the Turma with an existing ID
        turma.setId(1L);

        int databaseSizeBeforeCreate = turmaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoTurmaIsRequired() throws Exception {
        int databaseSizeBeforeTest = turmaRepository.findAll().size();
        // set the field null
        turma.setCodigoTurma(null);

        // Create the Turma, which fails.

        restTurmaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isBadRequest());

        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTurmas() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get all the turmaList
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(turma.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoTurma").value(hasItem(DEFAULT_CODIGO_TURMA)))
            .andExpect(jsonPath("$.[*].sala").value(hasItem(DEFAULT_SALA)))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(sameInstant(DEFAULT_ANO))));
    }

    @Test
    @Transactional
    void getTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        // Get the turma
        restTurmaMockMvc
            .perform(get(ENTITY_API_URL_ID, turma.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(turma.getId().intValue()))
            .andExpect(jsonPath("$.codigoTurma").value(DEFAULT_CODIGO_TURMA))
            .andExpect(jsonPath("$.sala").value(DEFAULT_SALA))
            .andExpect(jsonPath("$.ano").value(sameInstant(DEFAULT_ANO)));
    }

    @Test
    @Transactional
    void getNonExistingTurma() throws Exception {
        // Get the turma
        restTurmaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma
        Turma updatedTurma = turmaRepository.findById(turma.getId()).get();
        // Disconnect from session so that the updates on updatedTurma are not directly saved in db
        em.detach(updatedTurma);
        updatedTurma.codigoTurma(UPDATED_CODIGO_TURMA).sala(UPDATED_SALA).ano(UPDATED_ANO);

        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTurma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getCodigoTurma()).isEqualTo(UPDATED_CODIGO_TURMA);
        assertThat(testTurma.getSala()).isEqualTo(UPDATED_SALA);
        assertThat(testTurma.getAno()).isEqualTo(UPDATED_ANO);
    }

    @Test
    @Transactional
    void putNonExistingTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, turma.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma.sala(UPDATED_SALA);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getCodigoTurma()).isEqualTo(DEFAULT_CODIGO_TURMA);
        assertThat(testTurma.getSala()).isEqualTo(UPDATED_SALA);
        assertThat(testTurma.getAno()).isEqualTo(DEFAULT_ANO);
    }

    @Test
    @Transactional
    void fullUpdateTurmaWithPatch() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();

        // Update the turma using partial update
        Turma partialUpdatedTurma = new Turma();
        partialUpdatedTurma.setId(turma.getId());

        partialUpdatedTurma.codigoTurma(UPDATED_CODIGO_TURMA).sala(UPDATED_SALA).ano(UPDATED_ANO);

        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTurma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTurma))
            )
            .andExpect(status().isOk());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
        Turma testTurma = turmaList.get(turmaList.size() - 1);
        assertThat(testTurma.getCodigoTurma()).isEqualTo(UPDATED_CODIGO_TURMA);
        assertThat(testTurma.getSala()).isEqualTo(UPDATED_SALA);
        assertThat(testTurma.getAno()).isEqualTo(UPDATED_ANO);
    }

    @Test
    @Transactional
    void patchNonExistingTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, turma.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(turma))
            )
            .andExpect(status().isBadRequest());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTurma() throws Exception {
        int databaseSizeBeforeUpdate = turmaRepository.findAll().size();
        turma.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTurmaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(turma)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Turma in the database
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTurma() throws Exception {
        // Initialize the database
        turmaRepository.saveAndFlush(turma);

        int databaseSizeBeforeDelete = turmaRepository.findAll().size();

        // Delete the turma
        restTurmaMockMvc
            .perform(delete(ENTITY_API_URL_ID, turma.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Turma> turmaList = turmaRepository.findAll();
        assertThat(turmaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
