package com.easysettle.web.rest;

import com.easysettle.EasySettleApp;

import com.easysettle.domain.Transfers;
import com.easysettle.repository.TransfersRepository;
import com.easysettle.service.TransfersService;
import com.easysettle.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.easysettle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransfersResource REST controller.
 *
 * @see TransfersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasySettleApp.class)
public class TransfersResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private TransfersRepository transfersRepository;

    @Autowired
    private TransfersService transfersService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTransfersMockMvc;

    private Transfers transfers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TransfersResource transfersResource = new TransfersResource(transfersService);
        this.restTransfersMockMvc = MockMvcBuilders.standaloneSetup(transfersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfers createEntity(EntityManager em) {
        Transfers transfers = new Transfers()
            .amount(DEFAULT_AMOUNT);
        return transfers;
    }

    @Before
    public void initTest() {
        transfers = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransfers() throws Exception {
        int databaseSizeBeforeCreate = transfersRepository.findAll().size();

        // Create the Transfers
        restTransfersMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfers)))
            .andExpect(status().isCreated());

        // Validate the Transfers in the database
        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeCreate + 1);
        Transfers testTransfers = transfersList.get(transfersList.size() - 1);
        assertThat(testTransfers.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createTransfersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transfersRepository.findAll().size();

        // Create the Transfers with an existing ID
        transfers.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfersMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfers)))
            .andExpect(status().isBadRequest());

        // Validate the Transfers in the database
        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transfersRepository.findAll().size();
        // set the field null
        transfers.setAmount(null);

        // Create the Transfers, which fails.

        restTransfersMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfers)))
            .andExpect(status().isBadRequest());

        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransfers() throws Exception {
        // Initialize the database
        transfersRepository.saveAndFlush(transfers);

        // Get all the transfersList
        restTransfersMockMvc.perform(get("/api/transfers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfers.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getTransfers() throws Exception {
        // Initialize the database
        transfersRepository.saveAndFlush(transfers);

        // Get the transfers
        restTransfersMockMvc.perform(get("/api/transfers/{id}", transfers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transfers.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransfers() throws Exception {
        // Get the transfers
        restTransfersMockMvc.perform(get("/api/transfers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransfers() throws Exception {
        // Initialize the database
        transfersService.save(transfers);

        int databaseSizeBeforeUpdate = transfersRepository.findAll().size();

        // Update the transfers
        Transfers updatedTransfers = transfersRepository.findOne(transfers.getId());
        // Disconnect from session so that the updates on updatedTransfers are not directly saved in db
        em.detach(updatedTransfers);
        updatedTransfers
            .amount(UPDATED_AMOUNT);

        restTransfersMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTransfers)))
            .andExpect(status().isOk());

        // Validate the Transfers in the database
        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeUpdate);
        Transfers testTransfers = transfersList.get(transfersList.size() - 1);
        assertThat(testTransfers.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingTransfers() throws Exception {
        int databaseSizeBeforeUpdate = transfersRepository.findAll().size();

        // Create the Transfers

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransfersMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transfers)))
            .andExpect(status().isCreated());

        // Validate the Transfers in the database
        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransfers() throws Exception {
        // Initialize the database
        transfersService.save(transfers);

        int databaseSizeBeforeDelete = transfersRepository.findAll().size();

        // Get the transfers
        restTransfersMockMvc.perform(delete("/api/transfers/{id}", transfers.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transfers> transfersList = transfersRepository.findAll();
        assertThat(transfersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transfers.class);
        Transfers transfers1 = new Transfers();
        transfers1.setId(1L);
        Transfers transfers2 = new Transfers();
        transfers2.setId(transfers1.getId());
        assertThat(transfers1).isEqualTo(transfers2);
        transfers2.setId(2L);
        assertThat(transfers1).isNotEqualTo(transfers2);
        transfers1.setId(null);
        assertThat(transfers1).isNotEqualTo(transfers2);
    }
}
