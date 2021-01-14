package com.db.awmd.challenge.repository.impl;

import java.math.BigDecimal;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.api.v1.account.model.Notification;
import com.db.awmd.challenge.config.APPTransactionMessages;
import com.db.awmd.challenge.config.AppEnvParameters;
import com.db.awmd.challenge.config.TransactionStatus;
import com.db.awmd.challenge.config.TransactionType;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountAccessException;
import com.db.awmd.challenge.exception.AccountCreateException;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("checkstyle:Indentation")
@Slf4j
@Repository
public class AccountsRepositoryInMemory implements com.db.awmd.challenge.repository.AccountsRepository {
    private static final int           LOCK_TIMEOUT = 100;
    private final Map<String, Account> accounts     = new ConcurrentHashMap<>();
    private final AppEnvParameters     appEnvParamters;
    private final NotificationService  notificationService;

    @Autowired
    public AccountsRepositoryInMemory(AppEnvParameters applicationParameters, NotificationService notificationService) {
        this.appEnvParamters     = applicationParameters;
        this.notificationService = notificationService;
    }

    private Account checkIsAccountValid(String accountNo) {
        Account account = accounts.get(accountNo);

        log.info("com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.checkIsAccountValid Start");

        if (account == null) {
            log.info(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.checkIsAccountValid error ACCOUNT_NOT_AVAILABLE");

            throw new AccountAccessException(APPTransactionMessages.ACCOUNT_NOT_AVAILABLE.getMessage(),
                                             APPTransactionMessages.ACCOUNT_NOT_AVAILABLE.getErrorCode(),
                                             null);
        }

        log.info("com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.checkIsAccountValid done");

        return account;
    }

    private void checkIsSameAccounts(String accountNo, String toAccountNo) {
        log.info("com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.checkIsSameAccounts Start");

        if (accountNo.equalsIgnoreCase(toAccountNo)) {
            log.info(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.checkIsAccountValid error ACCOUNT_NOT_AVAILABLE");

            throw new AccountTransactionException(
                APPTransactionMessages.TRANSACTION_FAILED_NOT_ALLOW_SAME_ACCOUNT.getMessage(),
                APPTransactionMessages.TRANSACTION_FAILED_NOT_ALLOW_SAME_ACCOUNT.getErrorCode(),
                null);
        }
    }

    @Override
    public synchronized void clearAccounts() {
        try {
            accounts.clear();
        } catch (Exception ex) {
            throw new AccountTransactionException(APPTransactionMessages.SYSTEM_ERROR.getMessage(),
                                                  APPTransactionMessages.SYSTEM_ERROR.getErrorCode(),
                                                  null);
        }
    }

    @Override
    public void create(Account account) {
        log.info("com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.create START");
        validateAccountCreation(account);

        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);

        if (previousAccount != null) {
            log.error(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.create fail due to duplicate account");

            throw new AccountCreateException(APPTransactionMessages.ACCOUNT_DUPLICATE.getMessage(),
                                             APPTransactionMessages.ACCOUNT_DUPLICATE.getErrorCode(),
                                             null);
        }
    }

    private Boolean executeTransaction(final Account account, final BigDecimal amount,
                                       final TransactionType transactionType) {
        log.info("executeTransaction() start ");

        boolean transFlag = true;

        if (transactionType.equals(TransactionType.DEPOSIT)) {
            log.info("executeTransaction() DEPOSIT ");
            account.setLastCloseBalance(account.getBalance());
            account.setBalance(account.getBalance().add(amount));
        } else {
            log.info("executeTransaction() DEBIT ");
            account.setLastCloseBalance(account.getBalance());
            account.setBalance(account.getBalance().subtract(amount));
        }

        log.info("executeTransaction() Done ");

        return transFlag;
    }

    private void handelTransaction(BigDecimal amount, Account fromAccount, Account toAccount, String transactionId) {
        if (!this.executeTransaction(fromAccount, amount, TransactionType.DEBIT)
                 .equals(this.executeTransaction(toAccount, amount, TransactionType.DEPOSIT))) {
            this.sendNotifications(fromAccount, amount, transactionId, TransactionStatus.DEBIT_FAIL);
            this.sendNotifications(toAccount, amount, transactionId, TransactionStatus.DEPOSIT_FAIL);
            log.warn("Transaction {1} can be rollback due to some error ", transactionId);
            fromAccount.setBalance(fromAccount.getLastCloseBalance());
            toAccount.setBalance(toAccount.getLastCloseBalance());
            log.info("Transaction {1} can be rollback successfully done.", transactionId);
            log.error(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.handelTransaction executeTransaction failed.");

            throw new AccountTransactionException(APPTransactionMessages.TRANSACTION_FAILED_TO_ROLLBACK.getMessage(),
                                                  APPTransactionMessages.TRANSACTION_FAILED_TO_ROLLBACK.getErrorCode(),
                                                  null);
        }

        this.sendNotifications(fromAccount, amount, transactionId, TransactionStatus.DEBIT_SUCCESS);
        this.sendNotifications(toAccount, amount, transactionId, TransactionStatus.DEPOSIT_SUCCESS);
        log.info(
            "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.handelTransaction executeTransaction successful");
    }

    private void lockAndExecuteAction(BigDecimal amount, String transactionId, Account fromAccount, Account toAccount) {
        try {
            if (fromAccount.getLock().tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                try {
                    if (toAccount.getLock().tryLock(LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                        handelTransaction(amount, fromAccount, toAccount, transactionId);
                        log.info("AccountsRepositoryInMemory :: transferMoney DONE ");
                    }
                } finally {
                    toAccount.getLock().unlock();
                    log.info("AccountsRepositoryInMemory :: transferMoney unlock account DONE ");
                }
            }
        } catch (Exception e) {
            log.error("AccountsRepositoryInMemory :: transferMoney InterruptedException due to {0} ", e.getMessage());

            throw new AccountTransactionException(APPTransactionMessages.SYSTEM_ERROR.getMessage(),
                                                  APPTransactionMessages.SYSTEM_ERROR.getErrorCode(),
                                                  null);
        } finally {
            fromAccount.getLock().unlock();
            log.info("AccountsRepositoryInMemory :: transferMoney unlock account DONE ");
        }
    }

    private void sendNotifications(Account fromAccount, BigDecimal amount, String transactionId,
                                   TransactionStatus debitSuccess) {
        log.info("sendNotifications() Start");
        this.notificationService.notifyAboutTransfer(new Notification(fromAccount,
                                                                      amount,
                                                                      transactionId,
                                                                      debitSuccess));
    }

    @Override
    public String transferMoney(String accountNo, String toAccountNo, BigDecimal amount) {
        String  transactionId = UUID.randomUUID().toString();
        Account fromAccount   = checkIsAccountValid(accountNo);
        Account toAccount     = checkIsAccountValid(toAccountNo);

        checkIsSameAccounts(accountNo, toAccountNo);
        validateAmount(fromAccount, amount);
        log.info("AccountsRepositoryInMemory :: transferMoney START ");
        lockAndExecuteAction(amount, transactionId, fromAccount, toAccount);

        return transactionId;
    }

    private void validateAccountCreation(Account account) {
        log.info("validateAccountCreation(Account) validation started ");

        if (account.getBalance().compareTo(new BigDecimal(this.appEnvParamters.getAccountMinBalanceLimit())) < 0) {
            log.error(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.validateAccountCreation fail due to account limit amount",
                this.appEnvParamters.getAccountMinBalanceLimit());

            throw new AccountCreateException(
                APPTransactionMessages.ACCOUNT_TRANSACTION_MINIMUM_AMOUNT_LIMIT.getMessage(),
                APPTransactionMessages.ACCOUNT_TRANSACTION_MINIMUM_AMOUNT_LIMIT.getErrorCode(),
                null);
        }

        log.info("validateAccountCreation(Account) validation successful ");
    }

    private void validateAmount(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) <= 0) {
            throw new AccountTransactionException(APPTransactionMessages.ACCOUNT_TRANSACTION_LOW_BALANCE.getMessage(),
                                                  APPTransactionMessages.ACCOUNT_TRANSACTION_LOW_BALANCE.getErrorCode(),
                                                  null);
        }
    }

    @Override
    public Account getAccountDetails(final String accountId) {
        Account account = accounts.get(accountId);

        if (account == null) {
            log.error(
                "com.db.awmd.challenge.repository.impl.AccountsRepositoryInMemory.getAccountDetails fail due to account ACCOUNT_NOT_AVAILABLE");

            throw new AccountAccessException(APPTransactionMessages.ACCOUNT_NOT_AVAILABLE.getMessage(),
                                             APPTransactionMessages.ACCOUNT_NOT_AVAILABLE.getErrorCode(),
                                             null);
        }

        return account;
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
