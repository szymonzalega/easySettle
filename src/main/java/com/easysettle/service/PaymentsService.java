package com.easysettle.service;

import com.easysettle.domain.Payments;
import com.easysettle.repository.PaymentsRepository;
import com.easysettle.service.dto.NewPaymentRequest;
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
public class PaymentsService {

    private final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final PaymentsRepository paymentsRepository;

    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    public Payments newPayment(NewPaymentRequest newPaymentRequest){

        Payments payments = Payments.builder()
            .amount(newPaymentRequest.getAmount())
            .date(newPaymentRequest.getDate())
            .group_id(newPaymentRequest.getGroup_id())
            .payer_id(newPaymentRequest.getPayer_id())
            .name(newPaymentRequest.getName())
            .build();

        Payments result = paymentsRepository.save(payments);

        return result;
    }


    /**
     * Save a payments.
     *
     * @param payments the entity to save
     * @return the persisted entity
     */
    public Payments save(Payments payments) {
        log.debug("Request to save Payments : {}", payments);
        return paymentsRepository.save(payments);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities
     */
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
    public void delete(Long id) {
        log.debug("Request to delete Payments : {}", id);
        paymentsRepository.delete(id);
    }
}
