package com.easysettle.repository;

import com.easysettle.domain.Transfers;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Transfers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfersRepository extends JpaRepository<Transfers, Long> {

}
