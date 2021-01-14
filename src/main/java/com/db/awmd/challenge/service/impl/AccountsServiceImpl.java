package com.db.awmd.challenge.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountsServiceImpl implements AccountsService {
    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountsServiceImpl(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public void clearAccounts() {
        log.debug("com.db.awmd.challenge.service.impl.AccountsServiceImpl.clearAccounts START");
        this.accountsRepository.clearAccounts();
    }

    /** @param account */
    public void createAccount(Account account) {
        log.debug("com.db.awmd.challenge.service.impl.AccountsServiceImpl.createAccount START");
        this.accountsRepository.create(account);
    }

    @Override
    public String transferMoney(String fromAccountId, String toAccountId, BigDecimal amount) {
        log.debug("com.db.awmd.challenge.service.impl.AccountsServiceImpl.transferMoney START");

        return accountsRepository.transferMoney(fromAccountId, toAccountId, amount);
    }

    public Account getAccount(String accountId) {
        log.debug("com.db.awmd.challenge.service.impl.AccountsServiceImpl.getAccount START");

        return this.accountsRepository.getAccountDetails(accountId);
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
