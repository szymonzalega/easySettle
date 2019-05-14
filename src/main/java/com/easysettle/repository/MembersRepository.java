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

    @Query("select m from Members m where m.balance < :zero and m.groups.id = :groupId order by m.balance asc")
    List<Members> findByBalanceLessThanOrderByBalanceAsc(@Param("zero") Double zero, @Param("groupId") Long groupId);

    @Query("select m from Members m where m.balance > :zero and m.groups.id = :groupId order by m.balance desc")
    List<Members> findByBalanceGreaterThanOrderByBalanceDesc(@Param("zero") Double zero, @Param("groupId") Long groupId);

    List<Members> findMembersByGroups_Id(Long groupId);

    Members findMembersById(Long id);

}
