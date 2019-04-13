package com.easysettle.service.impl;

import com.easysettle.service.TransfersService;
import com.easysettle.domain.Transfers;
import com.easysettle.repository.TransfersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Transfers.
 */
@Service
@Transactional
public class TransfersServiceImpl implements TransfersService {

    private final Logger log = LoggerFactory.getLogger(TransfersServiceImpl.class);

    private final TransfersRepository transfersRepository;

    public TransfersServiceImpl(TransfersRepository transfersRepository) {
        this.transfersRepository = transfersRepository;
    }

    /**
     * Save a transfers.
     *
     * @param transfers the entity to save
     * @return the persisted entity
     */
    @Override
    public Transfers save(Transfers transfers) {
        log.debug("Request to save Transfers : {}", transfers);
        return transfersRepository.save(transfers);
    }

    /**
     * Get all the transfers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Transfers> findAll() {
        log.debug("Request to get all Transfers");
        return transfersRepository.findAll();
    }

    /**
     * Get one transfers by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Transfers findOne(Long id) {
        log.debug("Request to get Transfers : {}", id);
        return transfersRepository.findOne(id);
    }

    /**
     * Delete the transfers by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Transfers : {}", id);
        transfersRepository.delete(id);
    }
}
