package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;

public interface AccountsRepository {
    void clearAccounts();

    void create(Account account);

    String transferMoney(String accountNo, String toAccountNo, BigDecimal amount);

    Account getAccountDetails(String accountId);
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
