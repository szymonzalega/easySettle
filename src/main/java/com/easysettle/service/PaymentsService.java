package com.easysettle.service;

import com.easysettle.domain.Members;
import com.easysettle.domain.Payments;
import com.easysettle.domain.Transfers;
import com.easysettle.repository.MembersRepository;
import com.easysettle.repository.PaymentsRepository;
import com.easysettle.service.dto.NewPaymentRequest;
import com.easysettle.service.dto.PaymentsAllInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service Implementation for managing Payments.
 */
@Service
@Transactional
public class PaymentsService {

    private final Logger log = LoggerFactory.getLogger(PaymentsService.class);

    private final PaymentsRepository paymentsRepository;

    private final MembersRepository membersRepository;

    public PaymentsService(PaymentsRepository paymentsRepository, MembersRepository membersRepository) {
        this.paymentsRepository = paymentsRepository;
        this.membersRepository = membersRepository;
    }

    public Payments newPayment(NewPaymentRequest newPaymentRequest){

        Payments payments = Payments.builder()
            .amount(newPaymentRequest.getAmount())
            .date(newPaymentRequest.getDate())
            .groupId(newPaymentRequest.getGroup_id())
            .payer_id(newPaymentRequest.getPayer_id())
            .name(newPaymentRequest.getName())
            .build();

        Payments result = paymentsRepository.save(payments);

        return result;
    }

    public List<PaymentsAllInformation> getAllPayments(Long groupId){

        List<PaymentsAllInformation> allPaymentsList = new ArrayList<>();

        List<Payments> paymentsList = paymentsRepository.findPaymentsByGroupId(groupId);
        for (Payments payment : paymentsList){
            Members member = membersRepository.findMembersById(payment.getPayer_id());

            PaymentsAllInformation paymentsAllInformation = PaymentsAllInformation.builder()
                .amount(payment.getAmount())
                .name(payment.getName())
                .date(payment.getDate())
                .payerName(member.getName())
                .loanersNameList(createLoanersNameList(payment.getTransfers()))
                .build();

            allPaymentsList.add(paymentsAllInformation);
        }
        return allPaymentsList;
    }

    private List<String> createLoanersNameList(Set<Transfers> loanersIdList){
        List<String> loanersList = new ArrayList<>();
        for(Transfers transfer : loanersIdList){
            loanersList.add(transfer.getLoaner().getName());
        }
        return loanersList;
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
