package com.db.awmd.challenge;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.fail;

import com.db.awmd.challenge.config.APPTransactionMessages;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountCreateException;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.exception.BaseAppException;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {
    public static final BigDecimal MOCK_AMOUNT = new BigDecimal(2021);
    @Autowired
    private AccountsService        accountsService;

    @Test
    public void addAccount() throws Exception {
        Account account = new Account("Id-123", MOCK_AMOUNT);

        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);
        assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
    }

    @Test
    public void addAccount_failsOnDuplicateId() throws Exception {
        String  uniqueId = "Id-" + System.currentTimeMillis();
        Account account  = new Account(uniqueId, MOCK_AMOUNT);

        account.setBalance(BigDecimal.valueOf(1111));

        try {
            this.accountsService.createAccount(account);
            this.accountsService.createAccount(account);
            fail("Should have failed when account with duplicate account");
        } catch (AccountCreateException ex) {
            assertThat(ex.getMessage()).isEqualTo(APPTransactionMessages.ACCOUNT_DUPLICATE.getMessage());
        }
    }

    @Test
    public void addAccount_failsOnMinimumAmountSystemCheck() throws Exception {
        String  uniqueId = "Id-" + System.currentTimeMillis();
        Account account  = new Account(uniqueId, new BigDecimal(0));

        try {
            this.accountsService.createAccount(account);
            fail("Should have failed when account with required minimum amount ");
        } catch (AccountCreateException ex) {
            assertThat(ex.getMessage()).isEqualTo("Minimum amount validation check failed");
        }
    }

    @Test
    public void transfer_failsOnCurrentBalanceLessThenTransfer() throws Exception {
        try {
            String  uniqueId1 = "Id1-" + System.currentTimeMillis();
            Account account   = new Account(uniqueId1, MOCK_AMOUNT);

            account.setBalance(BigDecimal.valueOf(1111));
            this.accountsService.createAccount(account);

            String  uniqueId2 = "Id2-" + System.currentTimeMillis();
            Account account1  = new Account(uniqueId2, MOCK_AMOUNT);

            account1.setBalance(BigDecimal.valueOf(1111));
            this.accountsService.createAccount(account1);
            this.accountsService.transferMoney(account.getAccountId(),
                                               account1.getAccountId(),
                                               BigDecimal.valueOf(14440));
            fail("Should have failed when account balance is low then transfer amount");
        } catch (BaseAppException ex) {
            assertThat(ex.getErrorCode()).isEqualTo(
                APPTransactionMessages.ACCOUNT_TRANSACTION_LOW_BALANCE.getErrorCode());
        }
    }

    @Test
    public void transfer_failsOnNonNonExistAccount() throws Exception {
        try {
            String  uniqueId1 = "Id1-" + System.currentTimeMillis();
            Account account   = new Account(uniqueId1, MOCK_AMOUNT);

            account.setBalance(BigDecimal.valueOf(1111));
            this.accountsService.createAccount(account);

            String  uniqueId2 = "Id2-" + System.currentTimeMillis();
            Account account1  = new Account(uniqueId2, MOCK_AMOUNT);

            account1.setBalance(BigDecimal.valueOf(1111));
            this.accountsService.transferMoney(account.getAccountId(),
                                               account1.getAccountId(),
                                               BigDecimal.valueOf(1112));
            fail("Should have failed account is not not avaliable in system");
        } catch (BaseAppException ex) {
            assertThat(ex.getMessage()).isEqualTo("This account not exists in system.");
        }
    }

    @Test
    public void transfer_failsOnSameAccount() throws Exception {
        String  uniqueId = "Id-" + System.currentTimeMillis();
        Account account  = new Account(uniqueId, MOCK_AMOUNT);

        account.setBalance(BigDecimal.valueOf(1111));
        this.accountsService.createAccount(account);

        try {
            this.accountsService.transferMoney(account.getAccountId(), account.getAccountId(), BigDecimal.valueOf(111));
            fail("Should have failed when account with zero amount");
        } catch (AccountTransactionException ex) {
            assertThat(ex.getMessage()).isEqualTo("You can transfer money only to other account");
        }
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
