package com.easysettle.service;

import com.easysettle.domain.Members;
import com.easysettle.repository.MembersRepository;
import com.easysettle.service.dto.NewPaymentRequest;
import com.easysettle.service.dto.SettleDebtResult;
import org.decimal4j.util.DoubleRounder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service Implementation for managing Members.
 */
@Service
@Transactional
public class MembersService {

    private final Logger log = LoggerFactory.getLogger(MembersService.class);

    private final MembersRepository membersRepository;

    private static final Double ZERO = 0d;

    public MembersService(MembersRepository membersRepository) {
        this.membersRepository = membersRepository;
    }

    public List<Members> saveMembers(List<Members> membersList){
        List<Members> result = membersRepository.save(membersList);
        return result;
    }

    public void changeMemberBalance(NewPaymentRequest request) {
        membersRepository.changeMemberBalance(request.getAmount(), request.getPayer_id());
        Double amountPerMember = countAmountPerLoaner(request.getAmount(), request.getLoanersList());
        for (Long memberId : request.getLoanersList()) {
            membersRepository.changeMemberBalance(amountPerMember, memberId);
        }
    }

    public List<SettleDebtResult> settleDebts(){
        LinkedHashMap<Members, Double> positiveBalanceMap = getBalanceMap(true);
        LinkedHashMap<Members, Double> negativeBalanceMap = getBalanceMap(false);
        List<SettleDebtResult> settleDebtResultList = new ArrayList<>();

        while(positiveBalanceMap.size() != 0 && negativeBalanceMap.size() != 0){

            Double result = null;

            Map.Entry<Members, Double> positiveMapEntry = positiveBalanceMap.entrySet().iterator().next();
            Members positiveKey = positiveMapEntry.getKey();
            Double positiveValue = positiveMapEntry.getValue();

            Map.Entry<Members, Double> negativeMapEntry = negativeBalanceMap.entrySet().iterator().next();
            Members negativeKey = negativeMapEntry.getKey();
            Double negativeValue = negativeMapEntry.getValue();

            result = positiveValue + negativeValue;

            if(result < 0){
                positiveBalanceMap.remove(positiveKey);
                negativeMapEntry.setValue(result);
                settleDebtResultList.add(getTransactionsObject(negativeKey, positiveKey, positiveValue));

            } else if (result > 0){
                negativeBalanceMap.remove(negativeKey);
                positiveMapEntry.setValue(result);
                settleDebtResultList.add(getTransactionsObject(negativeKey, positiveKey, negativeValue));

            } else {
                positiveBalanceMap.remove(positiveKey);
                negativeBalanceMap.remove(negativeKey);
                settleDebtResultList.add(getTransactionsObject(negativeKey, positiveKey, negativeValue));
            }
        }
        return settleDebtResultList;
    }

    private SettleDebtResult getTransactionsObject(Members paymentFrom, Members paymentTo, Double amount){
        if(amount < 0){
            amount *= -1;
        }
        SettleDebtResult settleDebtResult = SettleDebtResult.builder()
            .paymentFrom(paymentFrom)
            .paymentTo(paymentTo)
            .amount(amount)
            .build();

        return settleDebtResult;
    }

    private LinkedHashMap<Members, Double> getBalanceMap(boolean isPositive){

        List<Members> membersList = new ArrayList<>();

        if(isPositive){
            membersList = membersRepository.findByBalanceGreaterThanOrderByBalanceDesc(ZERO);
        } else {
            membersList = membersRepository.findByBalanceLessThanOrderByBalanceAsc(ZERO);
        }

        LinkedHashMap<Members, Double> map = new LinkedHashMap<>();

        for (Members members : membersList){
            map.put(members, members.getBalance());
        }

        return map;
    }

    private Double countAmountPerLoaner(Double amount, List<Long> loanersList) {
        Double amountPerMember = DoubleRounder.round(amount / loanersList.size(), 2);
        Double oppositeAmount = amountPerMember * -1;
        return oppositeAmount;
    }

    /**
     * Save a members.
     *
     * @param members the entity to save
     * @return the persisted entity
     */
    public Members save(Members members) {
        log.debug("Request to save Members : {}", members);
        return membersRepository.save(members);
    }

    /**
     * Get all the members.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Members> findAll() {
        log.debug("Request to get all Members");
        return membersRepository.findAll();
    }

    /**
     * Get one members by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Members findOne(Long id) {
        log.debug("Request to get Members : {}", id);
        return membersRepository.findOne(id);
    }

    /**
     * Delete the members by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Members : {}", id);
        membersRepository.delete(id);
    }
}
