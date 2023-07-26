package com.ting.R3S.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost")
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/roles")
    public List<String> getAllRoles() {
        return Arrays.asList("admin", "manager");
    }

    @GetMapping("/account-table")
    public ResponseEntity<AccountTableDTO> getAccounts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "10") int size,
                                                       @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                       @RequestParam(value = "sortColumn", required = false, defaultValue = "accountId") String sortColumn,
                                                       @RequestParam(value = "sortDirection", required = false,defaultValue = "asc") String sortDirection,
                                                       @RequestParam(value = "role", required = false, defaultValue = "all") String role) {

        PageRequest pageRequest = PageRequest.of(page, size);

        Page<AccountDTO> accounts = accountService.searchAccounts(search, role, pageRequest, sortColumn, sortDirection);

        long recordsTotal = accountService.countAccounts();

        AccountTableDTO accountTableDTO = new AccountTableDTO(
                accounts.getNumber() * accounts.getSize(),
                accounts.getSize(),
                accounts.getNumber(),
                accounts.getTotalPages(),
                accounts.getTotalElements(),
                recordsTotal,
                accounts.getTotalElements(),
                true,
                accounts.getNumber() * accounts.getSize() + 1,
                accounts.getContent()
        );

        return ResponseEntity.ok().body(accountTableDTO);
    }

    @PostMapping("/deactive-accounts")
    public ResponseEntity<String> deactivateAccounts(@RequestBody List<Long> accountIds) {
        accountService.deactivateAccounts(accountIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Object[]>> getInactiveAccounts() {
        List<Object[]> inactiveAccounts = accountService.getInactiveAccounts();
        return new ResponseEntity<>(inactiveAccounts, HttpStatus.OK);
    }

    @PutMapping("/accounts/{accountId}/reactivate")
    public ResponseEntity<Void> reactivateAccount(@PathVariable Long accountId) {
        accountService.reactivateAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getaccount/{accountId}")
    public ResponseEntity<List<Object>> getAccountById(@PathVariable("accountId") Long accountId) {
        List<Object> accountDetail = accountService.getAccountById(accountId);
        return new ResponseEntity<>(accountDetail, HttpStatus.OK);
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<Void> updateAccountById(@RequestBody AccountDTO accountDTO, @PathVariable("accountId") Long accountId) {
        String editedEmail = accountDTO.getEmail();
        String editedRole = accountDTO.getRole();
        String editedPassword = accountDTO.getPassword();
        accountService.updateAccountById(editedEmail, editedRole, editedPassword, accountId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts")
    public Account saveAccount(@RequestBody Account account) {
        return accountService.saveAccount(account);
    }

    @GetMapping("/available-accounts")
    public List<AvailableAccountDTO> getAvailableAccounts() {
        return accountService.getAvailableAccounts();
    }

}
