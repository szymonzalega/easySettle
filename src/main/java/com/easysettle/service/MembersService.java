package com.easysettle.service;

import com.easysettle.domain.Members;
import java.util.List;

/**
 * Service Interface for managing Members.
 */
public interface MembersService {

    /**
     * Save a members.
     *
     * @param members the entity to save
     * @return the persisted entity
     */
    Members save(Members members);

    /**
     * Get all the members.
     *
     * @return the list of entities
     */
    List<Members> findAll();

    /**
     * Get the "id" members.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Members findOne(Long id);

    /**
     * Delete the "id" members.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
