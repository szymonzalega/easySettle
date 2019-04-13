package com.easysettle.service;

import com.easysettle.domain.Payments;
import java.util.List;

/**
 * Service Interface for managing Payments.
 */
public interface PaymentsService {

    /**
     * Save a payments.
     *
     * @param payments the entity to save
     * @return the persisted entity
     */
    Payments save(Payments payments);

    /**
     * Get all the payments.
     *
     * @return the list of entities
     */
    List<Payments> findAll();

    /**
     * Get the "id" payments.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Payments findOne(Long id);

    /**
     * Delete the "id" payments.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
