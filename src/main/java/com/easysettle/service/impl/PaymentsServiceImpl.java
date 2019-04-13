package com.easysettle.service.impl;

import com.easysettle.service.PaymentsService;
import com.easysettle.domain.Payments;
import com.easysettle.repository.PaymentsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Payments.
 */
@Service
@Transactional
public class PaymentsServiceImpl implements PaymentsService {

    private final Logger log = LoggerFactory.getLogger(PaymentsServiceImpl.class);

    private final PaymentsRepository paymentsRepository;

    public PaymentsServiceImpl(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    /**
     * Save a payments.
     *
     * @param payments the entity to save
     * @return the persisted entity
     */
    @Override
    public Payments save(Payments payments) {
        log.debug("Request to save Payments : {}", payments);
        return paymentsRepository.save(payments);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Payments> findAll() {
        log.debug("Request to get all Payments");
        return paymentsRepository.findAll();
    }

    /**
     * Get one payments by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Payments findOne(Long id) {
        log.debug("Request to get Payments : {}", id);
        return paymentsRepository.findOne(id);
    }

    /**
     * Delete the payments by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Payments : {}", id);
        paymentsRepository.delete(id);
    }
}
