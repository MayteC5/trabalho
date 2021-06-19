package br.com.trabalho.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.trabalho.IntegrationTest;
import br.com.trabalho.domain.Assunto;
import br.com.trabalho.repository.AssuntoRepository;
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
 * Integration tests for the {@link AssuntoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AssuntoResourceIT {

    private static final Integer DEFAULT_CODIGOASSUNTO = 1;
    private static final Integer UPDATED_CODIGOASSUNTO = 2;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Float DEFAULT_CARGA = 1F;
    private static final Float UPDATED_CARGA = 2F;

    private static final String ENTITY_API_URL = "/api/assuntos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AssuntoRepository assuntoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssuntoMockMvc;

    private Assunto assunto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assunto createEntity(EntityManager em) {
        Assunto assunto = new Assunto().codigoassunto(DEFAULT_CODIGOASSUNTO).nome(DEFAULT_NOME).carga(DEFAULT_CARGA);
        return assunto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Assunto createUpdatedEntity(EntityManager em) {
        Assunto assunto = new Assunto().codigoassunto(UPDATED_CODIGOASSUNTO).nome(UPDATED_NOME).carga(UPDATED_CARGA);
        return assunto;
    }

    @BeforeEach
    public void initTest() {
        assunto = createEntity(em);
    }

    @Test
    @Transactional
    void createAssunto() throws Exception {
        int databaseSizeBeforeCreate = assuntoRepository.findAll().size();
        // Create the Assunto
        restAssuntoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isCreated());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeCreate + 1);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getCodigoassunto()).isEqualTo(DEFAULT_CODIGOASSUNTO);
        assertThat(testAssunto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssunto.getCarga()).isEqualTo(DEFAULT_CARGA);
    }

    @Test
    @Transactional
    void createAssuntoWithExistingId() throws Exception {
        // Create the Assunto with an existing ID
        assunto.setId(1L);

        int databaseSizeBeforeCreate = assuntoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssuntoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoassuntoIsRequired() throws Exception {
        int databaseSizeBeforeTest = assuntoRepository.findAll().size();
        // set the field null
        assunto.setCodigoassunto(null);

        // Create the Assunto, which fails.

        restAssuntoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isBadRequest());

        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssuntos() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get all the assuntoList
        restAssuntoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assunto.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoassunto").value(hasItem(DEFAULT_CODIGOASSUNTO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].carga").value(hasItem(DEFAULT_CARGA.doubleValue())));
    }

    @Test
    @Transactional
    void getAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        // Get the assunto
        restAssuntoMockMvc
            .perform(get(ENTITY_API_URL_ID, assunto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assunto.getId().intValue()))
            .andExpect(jsonPath("$.codigoassunto").value(DEFAULT_CODIGOASSUNTO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.carga").value(DEFAULT_CARGA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingAssunto() throws Exception {
        // Get the assunto
        restAssuntoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto
        Assunto updatedAssunto = assuntoRepository.findById(assunto.getId()).get();
        // Disconnect from session so that the updates on updatedAssunto are not directly saved in db
        em.detach(updatedAssunto);
        updatedAssunto.codigoassunto(UPDATED_CODIGOASSUNTO).nome(UPDATED_NOME).carga(UPDATED_CARGA);

        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAssunto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getCodigoassunto()).isEqualTo(UPDATED_CODIGOASSUNTO);
        assertThat(testAssunto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssunto.getCarga()).isEqualTo(UPDATED_CARGA);
    }

    @Test
    @Transactional
    void putNonExistingAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assunto.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssuntoWithPatch() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto using partial update
        Assunto partialUpdatedAssunto = new Assunto();
        partialUpdatedAssunto.setId(assunto.getId());

        partialUpdatedAssunto.codigoassunto(UPDATED_CODIGOASSUNTO);

        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getCodigoassunto()).isEqualTo(UPDATED_CODIGOASSUNTO);
        assertThat(testAssunto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testAssunto.getCarga()).isEqualTo(DEFAULT_CARGA);
    }

    @Test
    @Transactional
    void fullUpdateAssuntoWithPatch() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();

        // Update the assunto using partial update
        Assunto partialUpdatedAssunto = new Assunto();
        partialUpdatedAssunto.setId(assunto.getId());

        partialUpdatedAssunto.codigoassunto(UPDATED_CODIGOASSUNTO).nome(UPDATED_NOME).carga(UPDATED_CARGA);

        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAssunto))
            )
            .andExpect(status().isOk());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
        Assunto testAssunto = assuntoList.get(assuntoList.size() - 1);
        assertThat(testAssunto.getCodigoassunto()).isEqualTo(UPDATED_CODIGOASSUNTO);
        assertThat(testAssunto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testAssunto.getCarga()).isEqualTo(UPDATED_CARGA);
    }

    @Test
    @Transactional
    void patchNonExistingAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assunto.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(assunto))
            )
            .andExpect(status().isBadRequest());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssunto() throws Exception {
        int databaseSizeBeforeUpdate = assuntoRepository.findAll().size();
        assunto.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssuntoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(assunto)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Assunto in the database
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssunto() throws Exception {
        // Initialize the database
        assuntoRepository.saveAndFlush(assunto);

        int databaseSizeBeforeDelete = assuntoRepository.findAll().size();

        // Delete the assunto
        restAssuntoMockMvc
            .perform(delete(ENTITY_API_URL_ID, assunto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Assunto> assuntoList = assuntoRepository.findAll();
        assertThat(assuntoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
