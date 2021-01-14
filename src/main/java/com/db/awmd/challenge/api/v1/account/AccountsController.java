package com.db.awmd.challenge.api.v1.account;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.db.awmd.challenge.api.v1.account.model.Transaction;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {
    private final AccountsService accountsService;

    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody
    @Valid Account account) {
        log.info("Creating account {}", account);
        this.accountsService.createAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<Object> deleteAccounts() {
        log.info("deleteAccounts start");
        this.accountsService.clearAccounts();

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/{accountId}/transactions")
    public String transactionTransfer(@RequestBody
    @Valid Transaction transaction) {
        log.info("Transactions for account {0}", transaction.getFromAccountId());

        return this.accountsService.transferMoney(transaction.getFromAccountId(),
                                                  transaction.getToAccountId(),
                                                  transaction.getAmount());
    }

    @GetMapping(path = "/{accountId}")
    public Account getAccount(@PathVariable
    @NotEmpty String accountId) {
        log.info("Retrieving account for id {}", accountId);

        return this.accountsService.getAccount(accountId);
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
