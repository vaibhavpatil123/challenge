package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;


import java.math.BigDecimal;

public interface AccountsService {
  void clearAccounts();

  void createAccount(Account account);

  String transferMoney(String from, String to, BigDecimal amount);

  Account getAccount(String accountId);
}

// ~ Formatted by Jindent --- http://www.jindent.com
