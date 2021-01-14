package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;

public interface AccountsService {
    void clearAccounts();

    void createAccount(Account account);

    String transferMoney(String from, String to, BigDecimal amount);

    Account getAccount(String accountId);
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
