package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.fake.DepositRequestBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.ViewTransactionHistory;
import com.dev.user_transaction_management_system.use_case.WithdrawingMoney;
import com.dev.user_transaction_management_system.use_case.dto.TransactionReceipt;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistory;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.dev.user_transaction_management_system.fake.AccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@Tag("INTEGRATION")
class TransactionHistoryControllerTest extends BankAccountTestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ViewTransactionHistory transactionRepository;

    @Autowired
    private UserAccountTestUtil userAccountUtil;

    @Autowired
    private DepositingMoney depositingMoney;

    @Autowired
    private WithdrawingMoney withdrawingMoney;

    private UserEntity entity;
    private String token;

    @BeforeEach
    void setUp() {
        String username="amir@gmail.com";
        String password = "@Abcd137845";

        entity = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password));

        token = userAccountUtil.signIn(username, password);
    }

    @Test
    void view_user_transaction_history_by_account_number() throws Exception {
        final String accountNumber = "0300654789123";
        initFakeTransactionsWith(accountNumber);


        final MvcResult mvcResult = mockMvc.perform(
                get("/api/transaction/history/{accountNumber}",accountNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        final List<TransactionHistory> transactionHistoryList = readTransactionHistoriesFrom(mvcResult);


        List<TransactionHistory> histories = transactionRepository.getHistoryByAccountNumber(accountNumber);


        assertThat(histories).isNotEmpty();
        assertThat(transactionHistoryList).containsAll(histories);
    }

    private void initFakeTransactionsWith(String accountNumber) {
        havingOpened(anAccount().enabled().withUserId(entity.getId())
                .withAccountNumber(accountNumber).withBalance(500));

        initDepositTransaction(accountNumber);
        initWithdrawalTransaction(accountNumber);
    }

    private void initWithdrawalTransaction(String accountNumber) {
        final String description = "transaction description";
        final WithdrawalRequest withdrawalRequest =
                new WithdrawalRequest(200,accountNumber,description);

        withdrawingMoney.withdraw(withdrawalRequest);
    }

    private void initDepositTransaction(String accountNumber) {
        final DepositRequest depositRequest = DepositRequestBuilder.aDepositRequest()
                .withAmount(300)
                .withAccountNumber(accountNumber)
                .withDescription("transaction description")
                .initiate();

        depositingMoney.deposit(depositRequest);
    }

    private List<TransactionHistory> readTransactionHistoriesFrom(MvcResult mvcResult)
            throws UnsupportedEncodingException, JsonProcessingException {

        final String response = mvcResult.getResponse().getContentAsString();


        final CollectionType valueType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, TransactionHistory.class);

        return objectMapper.readValue(response, valueType);
    }
}
