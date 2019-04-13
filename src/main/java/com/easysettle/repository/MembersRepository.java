package com.easysettle.repository;

import com.easysettle.domain.Members;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Members entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembersRepository extends JpaRepository<Members, Long> {

    @Modifying
    @Query("update Members m set balance = balance + :amount where id = :memberId")
    void changeMemberBalance(@Param("amount") Double amount, @Param("memberId") Long memberId);

    List<Members> findByBalanceLessThanOrderByBalanceAsc(Double zero);

    List<Members> findByBalanceGreaterThanOrderByBalanceDesc(Double zero);

}
