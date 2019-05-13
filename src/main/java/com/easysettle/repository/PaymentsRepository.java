package com.easysettle.repository;

import com.easysettle.domain.Payments;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Payments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    List<Payments> findPaymentsByName (Long groupId);

}
