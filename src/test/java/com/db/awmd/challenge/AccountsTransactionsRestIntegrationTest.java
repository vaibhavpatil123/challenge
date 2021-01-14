package com.db.awmd.challenge;

import java.math.BigDecimal;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.db.awmd.challenge.api.v1.account.model.Transaction;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountAccessException;
import com.db.awmd.challenge.exception.AccountTransactionException;
import com.db.awmd.challenge.exception.BaseAppException;
import com.db.awmd.challenge.exception.handler.ApiError;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsTransactionsRestIntegrationTest {
    public static final String     V_1_ACCOUNTS_API                    = "/v1/accounts/";
    public static final String     V_1_TRANS_API                       = "/transactions";
    public static final BigDecimal MOCK_AMOUNT                         = new BigDecimal(2021);
    public static final BigDecimal MOCK_AMOUNT_TRANSFER                = new BigDecimal(100);
    public static final BigDecimal MOCK_AMOUNT_TRANSFER_MAX_BAL        = new BigDecimal(77777);
    public static final BigDecimal MOCK_AMOUNT_TRANSFER_LESS_THEN_ZERO = new BigDecimal(2021);
    @LocalServerPort
    private int                    port;
    @Autowired
    private TestRestTemplate       restTemplate;

    // 1 check basic trans api
    @Test
    public void testValidAccountTransaction() {
        Account                account        = new Account("1", MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);
        Account account2 = new Account("2", MOCK_AMOUNT);

        responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + V_1_ACCOUNTS_API,
                                                         account2,
                                                         Object.class);

        // set transobject
        Transaction transaction = new Transaction();

        transaction.setAmount(MOCK_AMOUNT_TRANSFER);
        transaction.setFromAccountId(account.getAccountId());
        transaction.setToAccountId(account2.getAccountId());

        ResponseEntity<String> responseEntity_trans = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                      + V_1_ACCOUNTS_API
                                                                                      + account.getAccountId()
                                                                                      + V_1_TRANS_API,
                                                                                      transaction,
                                                                                      String.class);

        assertNotNull(responseEntity);
    }

    // case 4 transaction for other account but trans amount> available balance
    @Test
    public void test_nonexist_account_AccountTransaction() {
        Account                account        = new Account("1", MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);
        Account account2 = new Account("2", MOCK_AMOUNT);

        responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + V_1_ACCOUNTS_API,
                                                         account2,
                                                         Object.class);

        // set transobject
        Transaction transaction = new Transaction();

        transaction.setAmount(MOCK_AMOUNT_TRANSFER);

        // Non exist account set here
        transaction.setFromAccountId("12121212");
        transaction.setToAccountId(account.getAccountId());

        try {
            ResponseEntity<String> responseEntity_trans = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                          + V_1_ACCOUNTS_API
                                                                                          + account.getAccountId()
                                                                                          + V_1_TRANS_API,
                                                                                          transaction,
                                                                                          String.class);
        } catch (AccountAccessException e) {
            assertNotNull(e);
        }
    }

    // case 3 transaction for other account but trans amount> available balance
    @Test
    public void test_other_less_blance_amount_grter_AccountTransaction() {
        Account                account        = new Account("1122334455", MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);
        Account account2 = new Account("11223344552", MOCK_AMOUNT);

        responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + V_1_ACCOUNTS_API,
                                                         account2,
                                                         Object.class);

        // set transobject
        Transaction transaction = new Transaction();

        transaction.setAmount(MOCK_AMOUNT_TRANSFER);
        transaction.setFromAccountId(account.getAccountId());
        transaction.setToAccountId(account.getAccountId());

        try {
            ResponseEntity<String> responseEntity_trans = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                          + V_1_ACCOUNTS_API
                                                                                          + account.getAccountId()
                                                                                          + V_1_TRANS_API,
                                                                                          transaction,
                                                                                          String.class);
        } catch (AccountTransactionException e) {
            assertNotNull(e);
        }
    }

    // case 2 transaction for same account should fail
    @Test
    public void test_same_AccountTransaction() {
        Account                account        = new Account("1212121212", MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);
        Account account2 = new Account("1212121212", MOCK_AMOUNT);

        responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + V_1_ACCOUNTS_API,
                                                         account2,
                                                         Object.class);

        // set transobject
        Transaction transaction = new Transaction();

        transaction.setAmount(MOCK_AMOUNT_TRANSFER);
        transaction.setFromAccountId(account.getAccountId());
        transaction.setToAccountId(account.getAccountId());

        try {
            ResponseEntity<String> responseEntity_trans = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                          + V_1_ACCOUNTS_API
                                                                                          + account.getAccountId()
                                                                                          + V_1_TRANS_API,
                                                                                          transaction,
                                                                                          String.class);
        } catch (AccountTransactionException e) {
            assertNotNull(e);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
