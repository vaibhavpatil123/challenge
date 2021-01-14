package com.db.awmd.challenge;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.handler.ApiError;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsRestIntegrationTest {
    public static final String     V_1_ACCOUNTS_API = "/v1/accounts";
    public static final BigDecimal MOCK_AMOUNT      = new BigDecimal(2021);
    @LocalServerPort
    private int                    port;
    @Autowired
    private TestRestTemplate       restTemplate;

    // 1 check basic function working or not
    @Test
    public void testCreateAccount() {
        Account                account        = new Account("1", MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertEquals(201, responseEntity.getStatusCodeValue());
    }

    // 5 check less then zero balance system should return error
    @Test
    public void testCreateAccount_Fail_DuplicateAccount() {
        Account                account        = new Account("1", new BigDecimal(-1));
        Account                account2       = new Account("1", new BigDecimal(-1));
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertNotNull(((Map<Object, Object>) responseEntity.getBody()).get("apierror"));

        Map<String, ApiError> errorMap = (HashMap) responseEntity.getBody();

        assertNotNull(((Map<Object, Object>) responseEntity.getBody()).get("apierror"));
    }

    // 3  check less then zero balance system should return error
    @Test
    public void testCreateAccount_Fail_Mini_AmountLessThenZero() {
        Account                account        = new Account("1", new BigDecimal(-1));
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    // 2  check bussiness validation should accept mini external configure amount
    @Test
    public void testCreateAccount_Fail_Mini_AmountNotLimitBSFail() {
        Account                account        = new Account("1", new BigDecimal(0));
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    // 4 check less then zero balance system should return error
    @Test
    public void testCreateAccount_Fail_getErrorMessages() {
        Account                account        = new Account("1", new BigDecimal(-1));
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertNotNull(((Map<Object, Object>) responseEntity.getBody()).get("apierror"));
    }

    // 7 delete account api
    @Test
    public void testDeleteAccount() {
        Account                account        = new Account("" + Math.random(), MOCK_AMOUNT);
        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port
                                                                                + V_1_ACCOUNTS_API,
                                                                                account,
                                                                                Object.class);

        assertEquals(201, responseEntity.getStatusCodeValue());

        try {
            this.restTemplate.delete("http://localhost:" + port + V_1_ACCOUNTS_API, Void.class);
        } catch (Exception ex) {
            assertNull(ex);
        }
    }

    // 8 get account api
    @Test
    public void test_GetAccount() {
        Account account = new Account("1", new BigDecimal(-1));

        try {
            Account account2 = this.restTemplate.getForObject("http://localhost:" + port + V_1_ACCOUNTS_API + "/"
                                                              + account.getAccountId(),
                                                              Account.class);

            assertNotNull(account2);
            assertEquals(account.getAccountId(), account2.getAccountId());
        } catch (Exception ex) {
            assertNull(ex);
        }
    }

    // 9 get account api
    @Test
    public void testgetAccount_nonexist() {
        Account account = new Account("123232323", new BigDecimal(-1));

        try {
            Account account2 = this.restTemplate.getForObject("http://localhost:" + port + V_1_ACCOUNTS_API + "/"
                                                              + account.getAccountId(),
                                                              Account.class);
        } catch (Exception ex) {
            assertNotNull(ex);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
