package com.easysettle.web.rest;

import com.easysettle.EasySettleApp;

import com.easysettle.domain.Groups;
import com.easysettle.repository.GroupsRepository;
import com.easysettle.service.GroupsService;
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
 * Test class for the GroupsResource REST controller.
 *
 * @see GroupsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasySettleApp.class)
public class GroupsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private GroupsRepository groupsRepository;

    @Autowired
    private GroupsService groupsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGroupsMockMvc;

    private Groups groups;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GroupsResource groupsResource = new GroupsResource(groupsService);
        this.restGroupsMockMvc = MockMvcBuilders.standaloneSetup(groupsResource)
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
    public static Groups createEntity(EntityManager em) {
        Groups groups = new Groups()
            .name(DEFAULT_NAME);
        return groups;
    }

    @Before
    public void initTest() {
        groups = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroups() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate + 1);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGroupsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupsRepository.findAll().size();

        // Create the Groups with an existing ID
        groups.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupsRepository.findAll().size();
        // set the field null
        groups.setName(null);

        // Create the Groups, which fails.

        restGroupsMockMvc.perform(post("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isBadRequest());

        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get all the groupsList
        restGroupsMockMvc.perform(get("/api/groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groups.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getGroups() throws Exception {
        // Initialize the database
        groupsRepository.saveAndFlush(groups);

        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", groups.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(groups.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGroups() throws Exception {
        // Get the groups
        restGroupsMockMvc.perform(get("/api/groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Update the groups
        Groups updatedGroups = groupsRepository.findOne(groups.getId());
        // Disconnect from session so that the updates on updatedGroups are not directly saved in db
        em.detach(updatedGroups);
        updatedGroups
            .name(UPDATED_NAME);

        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroups)))
            .andExpect(status().isOk());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate);
        Groups testGroups = groupsList.get(groupsList.size() - 1);
        assertThat(testGroups.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingGroups() throws Exception {
        int databaseSizeBeforeUpdate = groupsRepository.findAll().size();

        // Create the Groups

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGroupsMockMvc.perform(put("/api/groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(groups)))
            .andExpect(status().isCreated());

        // Validate the Groups in the database
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGroups() throws Exception {
        // Initialize the database
        groupsService.save(groups);

        int databaseSizeBeforeDelete = groupsRepository.findAll().size();

        // Get the groups
        restGroupsMockMvc.perform(delete("/api/groups/{id}", groups.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Groups> groupsList = groupsRepository.findAll();
        assertThat(groupsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Groups.class);
        Groups groups1 = new Groups();
        groups1.setId(1L);
        Groups groups2 = new Groups();
        groups2.setId(groups1.getId());
        assertThat(groups1).isEqualTo(groups2);
        groups2.setId(2L);
        assertThat(groups1).isNotEqualTo(groups2);
        groups1.setId(null);
        assertThat(groups1).isNotEqualTo(groups2);
    }
}
