package com.ting.R3S.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    //find account by email
    Account findByEmail(String email);

    //filter account details based on search term, sorting, roles, columns
//    @Query("SELECT new com.ting.R3S.Account.AccountDTO(a.accountId, a.email, a.password, a.role, COALESCE(r.restaurantName, ''), COALESCE(r.openTime, '00:00:00'), COALESCE(r.closeTime, '00:00:00'), COALESCE(r.status, '')) " +
//            "FROM Account a LEFT JOIN a.restaurant r " +
//            "WHERE (CAST(a.accountId AS string) LIKE %:searchTerm% OR a.email LIKE %:searchTerm%) " +
//            "AND ((:role = 'all' AND a.role IS NOT NULL) OR a.role = :role)  " +
//            "AND a.status = 'active' " +
//            "ORDER BY " +
//            "CASE WHEN (:sortColumn is null OR :sortDirection is null) THEN a.accountId END ASC, " +
//            "CASE WHEN :sortColumn = 'email' AND :sortDirection = 'asc' THEN a.email END ASC, " +
//            "CASE WHEN :sortColumn = 'email' AND :sortDirection = 'desc' THEN a.email END DESC, " +
//            "CASE WHEN :sortColumn = 'accountId' AND :sortDirection = 'asc' THEN a.accountId END ASC, " +
//            "CASE WHEN :sortColumn = 'accountId' AND :sortDirection = 'desc' THEN a.accountId END DESC")
//    Page<AccountDTO> searchAccounts(
//            @Param("searchTerm") String searchTerm,
//            @Param("role") String role,
//            Pageable pageable,
//            @Param("sortColumn") String sortColumn,
//            @Param("sortDirection") String sortDirection);
//

    @Query("SELECT new com.ting.R3S.Account.AccountDTO(a.accountId, a.email, a.password, a.role, COALESCE(r.restaurantName, ''), COALESCE(r.openTime, '00:00:00'), COALESCE(r.closeTime, '00:00:00'), COALESCE(r.status, '')) " +
            "FROM Account a " +
            "LEFT JOIN a.restaurant r " +
            "WHERE (CAST(a.accountId AS string) LIKE %:searchTerm% OR a.email LIKE %:searchTerm%) " +
            "AND (IFNULL(:role, '') = 'all' OR a.role = :role) " +
            "AND a.status = 'active' " +
            "ORDER BY " +
            "CASE WHEN (:sortColumn is null OR :sortDirection is null) THEN a.accountId END ASC, " +
            "CASE WHEN :sortColumn = 'email' AND :sortDirection = 'asc' THEN a.email END ASC, " +
            "CASE WHEN :sortColumn = 'email' AND :sortDirection = 'desc' THEN a.email END DESC, " +
            "CASE WHEN :sortColumn = 'accountId' AND :sortDirection = 'asc' THEN a.accountId END ASC, " +
            "CASE WHEN :sortColumn = 'accountId' AND :sortDirection = 'desc' THEN a.accountId END DESC")
    Page<AccountDTO> searchAccounts(
            @Param("searchTerm") String searchTerm,
            @Param("role") String role,
            Pageable pageable,
            @Param("sortColumn") String sortColumn,
            @Param("sortDirection") String sortDirection);


    //find the inactive account(s)
    @Query("SELECT a.accountId, a.email FROM Account a WHERE a.status = 'inactive'")
    List<Object[]> findInactiveAccounts();

    @Query("SELECT new com.ting.R3S.Account.AvailableAccountDTO(a.accountId, a.email) " +
            "FROM Account a " +
            "LEFT JOIN Restaurant r ON a.accountId = r.account.accountId " +
            "WHERE r.account.accountId IS NULL AND a.role <> 'admin'")
    List<AvailableAccountDTO> findAvailableAccounts();

}
