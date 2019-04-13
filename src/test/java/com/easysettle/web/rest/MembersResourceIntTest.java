package com.easysettle.web.rest;

import com.easysettle.EasySettleApp;

import com.easysettle.domain.Members;
import com.easysettle.repository.MembersRepository;
import com.easysettle.service.MembersService;
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
 * Test class for the MembersResource REST controller.
 *
 * @see MembersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasySettleApp.class)
public class MembersResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private MembersService membersService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMembersMockMvc;

    private Members members;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MembersResource membersResource = new MembersResource(membersService);
        this.restMembersMockMvc = MockMvcBuilders.standaloneSetup(membersResource)
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
    public static Members createEntity(EntityManager em) {
        Members members = new Members()
            .name(DEFAULT_NAME)
            .balance(DEFAULT_BALANCE);
        return members;
    }

    @Before
    public void initTest() {
        members = createEntity(em);
    }

    @Test
    @Transactional
    public void createMembers() throws Exception {
        int databaseSizeBeforeCreate = membersRepository.findAll().size();

        // Create the Members
        restMembersMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(members)))
            .andExpect(status().isCreated());

        // Validate the Members in the database
        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeCreate + 1);
        Members testMembers = membersList.get(membersList.size() - 1);
        assertThat(testMembers.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMembers.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createMembersWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = membersRepository.findAll().size();

        // Create the Members with an existing ID
        members.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembersMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(members)))
            .andExpect(status().isBadRequest());

        // Validate the Members in the database
        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = membersRepository.findAll().size();
        // set the field null
        members.setName(null);

        // Create the Members, which fails.

        restMembersMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(members)))
            .andExpect(status().isBadRequest());

        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        membersRepository.saveAndFlush(members);

        // Get all the membersList
        restMembersMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(members.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.doubleValue())));
    }

    @Test
    @Transactional
    public void getMembers() throws Exception {
        // Initialize the database
        membersRepository.saveAndFlush(members);

        // Get the members
        restMembersMockMvc.perform(get("/api/members/{id}", members.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(members.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMembers() throws Exception {
        // Get the members
        restMembersMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembers() throws Exception {
        // Initialize the database
        membersService.save(members);

        int databaseSizeBeforeUpdate = membersRepository.findAll().size();

        // Update the members
        Members updatedMembers = membersRepository.findOne(members.getId());
        // Disconnect from session so that the updates on updatedMembers are not directly saved in db
        em.detach(updatedMembers);
        updatedMembers
            .name(UPDATED_NAME)
            .balance(UPDATED_BALANCE);

        restMembersMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMembers)))
            .andExpect(status().isOk());

        // Validate the Members in the database
        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeUpdate);
        Members testMembers = membersList.get(membersList.size() - 1);
        assertThat(testMembers.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMembers.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingMembers() throws Exception {
        int databaseSizeBeforeUpdate = membersRepository.findAll().size();

        // Create the Members

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMembersMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(members)))
            .andExpect(status().isCreated());

        // Validate the Members in the database
        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMembers() throws Exception {
        // Initialize the database
        membersService.save(members);

        int databaseSizeBeforeDelete = membersRepository.findAll().size();

        // Get the members
        restMembersMockMvc.perform(delete("/api/members/{id}", members.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Members> membersList = membersRepository.findAll();
        assertThat(membersList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Members.class);
        Members members1 = new Members();
        members1.setId(1L);
        Members members2 = new Members();
        members2.setId(members1.getId());
        assertThat(members1).isEqualTo(members2);
        members2.setId(2L);
        assertThat(members1).isNotEqualTo(members2);
        members1.setId(null);
        assertThat(members1).isNotEqualTo(members2);
    }
}
