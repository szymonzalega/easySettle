package com.easysettle.repository;

import com.easysettle.domain.Members;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Members entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembersRepository extends JpaRepository<Members, Long> {

}
