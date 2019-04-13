package com.easysettle.service;

import com.easysettle.domain.Members;
import com.easysettle.domain.Payments;
import com.easysettle.domain.Transfers;
import com.easysettle.repository.TransfersRepository;
import com.easysettle.service.dto.NewPaymentRequest;
import org.decimal4j.util.DoubleRounder;
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
public class TransfersService {

    private final Logger log = LoggerFactory.getLogger(TransfersService.class);

    private final TransfersRepository transfersRepository;

    public TransfersService(TransfersRepository transfersRepository) {
        this.transfersRepository = transfersRepository;
    }

    public void saveTransfers(Payments payments, NewPaymentRequest newPaymentRequest){

        Integer loanersAmount = newPaymentRequest.getLoanersList().size();
        Double amountPerMember = DoubleRounder.round(newPaymentRequest.getAmount() / loanersAmount, 2);

        Members payer = new Members();
        payer.setId(newPaymentRequest.getPayer_id());

        for (Long memberId : newPaymentRequest.getLoanersList()){

            Members loaner = new Members();
            loaner.setId(memberId);

            Transfers transfers = Transfers.builder()
                .payments(payments)
                .amount(amountPerMember)
                .payer(payer)
                .loaner(loaner)
                .build();

            Transfers result = transfersRepository.save(transfers);
        }
    }

    /**
     * Save a transfers.
     *
     * @param transfers the entity to save
     * @return the persisted entity
     */
    public Transfers save(Transfers transfers) {
        log.debug("Request to save Transfers : {}", transfers);
        return transfersRepository.save(transfers);
    }

    /**
     * Get all the transfers.
     *
     * @return the list of entities
     */
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
    public void delete(Long id) {
        log.debug("Request to delete Transfers : {}", id);
        transfersRepository.delete(id);
    }
}
