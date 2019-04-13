package com.easysettle.service;

import com.easysettle.domain.Transfers;
import java.util.List;

/**
 * Service Interface for managing Transfers.
 */
public interface TransfersService {

    /**
     * Save a transfers.
     *
     * @param transfers the entity to save
     * @return the persisted entity
     */
    Transfers save(Transfers transfers);

    /**
     * Get all the transfers.
     *
     * @return the list of entities
     */
    List<Transfers> findAll();

    /**
     * Get the "id" transfers.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Transfers findOne(Long id);

    /**
     * Delete the "id" transfers.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
