package com.easysettle.service;

import com.easysettle.domain.Groups;
import java.util.List;

/**
 * Service Interface for managing Groups.
 */
public interface GroupsService {

    /**
     * Save a groups.
     *
     * @param groups the entity to save
     * @return the persisted entity
     */
    Groups save(Groups groups);

    /**
     * Get all the groups.
     *
     * @return the list of entities
     */
    List<Groups> findAll();

    /**
     * Get the "id" groups.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Groups findOne(Long id);

    /**
     * Delete the "id" groups.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
