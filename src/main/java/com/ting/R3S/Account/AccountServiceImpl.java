package com.ting.R3S.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    public long countAccounts() {
        return accountRepository.count();
    }

    @Override
    public Page<AccountDTO> searchAccounts(String searchTerm, String role, Pageable pageable, String sortColumn, String sortDirection) {
           return accountRepository.searchAccounts(searchTerm, role, pageable, sortColumn, sortDirection);
    }

    @Override
    @Transactional
    public void deactivateAccounts(List<Long> accountIds) {
        List<Account> accounts = accountRepository.findAllById(accountIds);
        for (Account account : accounts) {
            account.setStatus("inactive");
        }
        accountRepository.saveAll(accounts);
    }

    @Override
    public List<Object[]> getInactiveAccounts() {
        return accountRepository.findInactiveAccounts();
    }

    @Override
    public void reactivateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        account.setStatus("active");
        accountRepository.save(account);
    }

    @Override
    public void updateAccountById(String editedEmail, String editedRole, String editedPassword, Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        account.setEmail(editedEmail);
        account.setRole(editedRole);
        account.setPassword(editedPassword);
        accountRepository.save(account);
    }

    @Override
    public List<Object> getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new IllegalArgumentException("Invalid account ID"));
        if (account != null) {
            List<Object> accountDetails = new ArrayList<>();
            accountDetails.add(account.getAccountId());
            accountDetails.add(account.getEmail());
            accountDetails.add(account.getRole());
            accountDetails.add(account.getPassword());
            return accountDetails;
        }
        return null;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account saveAccount(Account account) {
        // Disable foreign key constraint checks
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");

        Account newAccount = new Account();
        newAccount.setEmail(account.getEmail());
        newAccount.setPassword(account.getPassword());
        newAccount.setRole(account.getRole());
        newAccount.setStatus(account.getStatus());

        // Save the account
        newAccount = accountRepository.save(newAccount);

        // Enable foreign key constraint checks
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");

        return newAccount;
    }

    @Override
    public List<AvailableAccountDTO> getAvailableAccounts() {
        return accountRepository.findAvailableAccounts();
    }

}
