package com.easysettle.service.impl;

import com.easysettle.service.GroupsService;
import com.easysettle.domain.Groups;
import com.easysettle.repository.GroupsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Groups.
 */
@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {

    private final Logger log = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsRepository groupsRepository;

    public GroupsServiceImpl(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    @Override
    public Groups save(Groups groups) {
        log.debug("Request to save Groups : {}", groups);
        return groupsRepository.save(groups);
    }

    /**
     * Get all the groups.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Groups> findAll() {
        log.debug("Request to get all Groups");
        return groupsRepository.findAll();
    }

    /**
     * Get one groups by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Groups findOne(Long id) {
        log.debug("Request to get Groups : {}", id);
        return groupsRepository.findOne(id);
    }

    /**
     * Delete the groups by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Groups : {}", id);
        groupsRepository.delete(id);
    }
}
