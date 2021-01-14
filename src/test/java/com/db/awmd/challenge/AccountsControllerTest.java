package com.db.awmd.challenge;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountsControllerTest {
    private MockMvc               mockMvc;
    @Autowired
    private AccountsService       accountsService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void clearAccount() throws Exception {
        this.mockMvc.perform(delete("/v1/accounts/")).andExpect(status().isAccepted());
    }

    @Test
    public void createAccount() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"Id-1243\",\"balance\":1000}"))
                    .andExpect(status().isCreated());

        Account account = accountsService.getAccount("Id-1243");

        assertThat(account.getAccountId()).isEqualTo("Id-1243");
        assertThat(account.getBalance()).isEqualByComparingTo("1000");
    }

    @Test
    public void createAccountEmptyAccountId() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"\",\"balance\":1000}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNegativeBalance() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"Id-123\",\"balance\":-1000}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNoAccountId() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"balance\":1000}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNoBalance() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"Id-123\"}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void createAccountNoBody() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void createDuplicateAccount() throws Exception {
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"Id-123\",\"balance\":1000}"))
                    .andExpect(status().isCreated());
        this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
                                                 .content("{\"accountId\":\"Id-123\",\"balance\":1000}"))
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void fail_clearAccount() throws Exception {
        this.mockMvc.perform(delete("/v1/accounts/12")).andExpect(status().isMethodNotAllowed());
    }

    @Before
    public void prepareMockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

        // Reset the existing accounts before each test.
        // accountsService.getAccountsRepository().clearAccounts();
    }

    @Test
    public void getAccount() throws Exception {
        String  uniqueAccountId = "Id-" + System.currentTimeMillis();
        Account account         = new Account(uniqueAccountId, new BigDecimal("123.45"));

        this.accountsService.createAccount(account);
        this.mockMvc.perform(get("/v1/accounts/" + uniqueAccountId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("{\"accountId\":\"" + uniqueAccountId + "\",\"balance\":123.45}"));
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
