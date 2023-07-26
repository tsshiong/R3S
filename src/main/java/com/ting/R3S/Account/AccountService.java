package com.ting.R3S.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {

    long countAccounts();

    Page<AccountDTO> searchAccounts(String searchTerm, String role, Pageable pageable, String sortColumn, String sortDirection);

    void deactivateAccounts(List<Long> accountIds);

    List<Object[]> getInactiveAccounts();

    void reactivateAccount(Long accountId);

    void updateAccountById(String editedEmail, String editedRole, String editedPassword, Long accountId);

    List<Object> getAccountById(Long accountId);

    Account saveAccount(Account account);

    List<AvailableAccountDTO> getAvailableAccounts();

}
