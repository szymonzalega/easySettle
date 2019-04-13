package com.easysettle.web.rest;

import com.easysettle.EasySettleApp;

import com.easysettle.domain.Payments;
import com.easysettle.repository.PaymentsRepository;
import com.easysettle.service.PaymentsService;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.easysettle.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaymentsResource REST controller.
 *
 * @see PaymentsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasySettleApp.class)
public class PaymentsResourceIntTest {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_GROUP_ID = 1L;
    private static final Long UPDATED_GROUP_ID = 2L;

    private static final Long DEFAULT_PAYER_ID = 1L;
    private static final Long UPDATED_PAYER_ID = 2L;

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentsMockMvc;

    private Payments payments;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentsResource paymentsResource = new PaymentsResource(paymentsService);
        this.restPaymentsMockMvc = MockMvcBuilders.standaloneSetup(paymentsResource)
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
    public static Payments createEntity(EntityManager em) {
        Payments payments = new Payments()
            .amount(DEFAULT_AMOUNT)
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE)
            .group_id(DEFAULT_GROUP_ID)
            .payer_id(DEFAULT_PAYER_ID);
        return payments;
    }

    @Before
    public void initTest() {
        payments = createEntity(em);
    }

    @Test
    @Transactional
    public void createPayments() throws Exception {
        int databaseSizeBeforeCreate = paymentsRepository.findAll().size();

        // Create the Payments
        restPaymentsMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isCreated());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeCreate + 1);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayments.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPayments.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPayments.getGroup_id()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testPayments.getPayer_id()).isEqualTo(DEFAULT_PAYER_ID);
    }

    @Test
    @Transactional
    public void createPaymentsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentsRepository.findAll().size();

        // Create the Payments with an existing ID
        payments.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentsMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setAmount(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGroup_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setGroup_id(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPayer_idIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentsRepository.findAll().size();
        // set the field null
        payments.setPayer_id(null);

        // Create the Payments, which fails.

        restPaymentsMockMvc.perform(post("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isBadRequest());

        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get all the paymentsList
        restPaymentsMockMvc.perform(get("/api/payments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payments.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].group_id").value(hasItem(DEFAULT_GROUP_ID.intValue())))
            .andExpect(jsonPath("$.[*].payer_id").value(hasItem(DEFAULT_PAYER_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPayments() throws Exception {
        // Initialize the database
        paymentsRepository.saveAndFlush(payments);

        // Get the payments
        restPaymentsMockMvc.perform(get("/api/payments/{id}", payments.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(payments.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.group_id").value(DEFAULT_GROUP_ID.intValue()))
            .andExpect(jsonPath("$.payer_id").value(DEFAULT_PAYER_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPayments() throws Exception {
        // Get the payments
        restPaymentsMockMvc.perform(get("/api/payments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePayments() throws Exception {
        // Initialize the database
        paymentsService.save(payments);

        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();

        // Update the payments
        Payments updatedPayments = paymentsRepository.findOne(payments.getId());
        // Disconnect from session so that the updates on updatedPayments are not directly saved in db
        em.detach(updatedPayments);
        updatedPayments
            .amount(UPDATED_AMOUNT)
            .name(UPDATED_NAME)
            .date(UPDATED_DATE)
            .group_id(UPDATED_GROUP_ID)
            .payer_id(UPDATED_PAYER_ID);

        restPaymentsMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPayments)))
            .andExpect(status().isOk());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate);
        Payments testPayments = paymentsList.get(paymentsList.size() - 1);
        assertThat(testPayments.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayments.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPayments.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPayments.getGroup_id()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testPayments.getPayer_id()).isEqualTo(UPDATED_PAYER_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingPayments() throws Exception {
        int databaseSizeBeforeUpdate = paymentsRepository.findAll().size();

        // Create the Payments

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentsMockMvc.perform(put("/api/payments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(payments)))
            .andExpect(status().isCreated());

        // Validate the Payments in the database
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePayments() throws Exception {
        // Initialize the database
        paymentsService.save(payments);

        int databaseSizeBeforeDelete = paymentsRepository.findAll().size();

        // Get the payments
        restPaymentsMockMvc.perform(delete("/api/payments/{id}", payments.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Payments> paymentsList = paymentsRepository.findAll();
        assertThat(paymentsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payments.class);
        Payments payments1 = new Payments();
        payments1.setId(1L);
        Payments payments2 = new Payments();
        payments2.setId(payments1.getId());
        assertThat(payments1).isEqualTo(payments2);
        payments2.setId(2L);
        assertThat(payments1).isNotEqualTo(payments2);
        payments1.setId(null);
        assertThat(payments1).isNotEqualTo(payments2);
    }
}
