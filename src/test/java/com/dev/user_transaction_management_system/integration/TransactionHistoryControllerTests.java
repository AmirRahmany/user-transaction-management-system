package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.DepositRequestBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.use_case.DepositingMoney;
import com.dev.user_transaction_management_system.use_case.ViewTransactionHistory;
import com.dev.user_transaction_management_system.use_case.WithdrawingMoney;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.dev.user_transaction_management_system.use_case.dto.TransactionHistory;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Tag("INTEGRATION")
@Transactional
class TransactionHistoryControllerTests {

    public static final String TRANSACTION_DESCRIPTION = "transaction description";
    public static final String ACCOUNT_NUMBER = "0300654789123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ViewTransactionHistory transactionRepository;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @Autowired
    private DepositingMoney depositingMoney;

    @Autowired
    private WithdrawingMoney withdrawingMoney;

    @Autowired
    private BankAccountTestHelper bankAccountHelper;

    private User userAccount;
    private String token;

    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.givenActivatedSignedInUserWithToken();
        token = userAndToken.token();
        userAccount = userAndToken.user();
    }

    @Test
    void return_user_transaction_history_for_given_account_number() throws Exception {
        populateTransactionHistoryFor(ACCOUNT_NUMBER);

        final MvcResult mvcResult = mockMvc.perform(
                get("/api/transaction/history/{accountNumber}", ACCOUNT_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();

        final List<TransactionHistory> transactionHistoryList = readTransactionHistoriesFrom(mvcResult);
        List<TransactionHistory> dbTransactions = transactionRepository.getHistoryByAccountNumber(ACCOUNT_NUMBER);


        assertThat(transactionHistoryList).containsAll(dbTransactions);
        assertThat(dbTransactions)
                .as("Expected at least one transaction in DB for account: %s", ACCOUNT_NUMBER)
                .isNotEmpty();
    }

    private void populateTransactionHistoryFor(String accountNumber) {
        bankAccountHelper.havingOpened(anAccount().enabled().withUser(userAccount)
                .withAccountNumber(accountNumber)
                .withBalance(500));

        depositFundsInto(accountNumber);
        withdrawFundsFrom(accountNumber);
    }

    private void withdrawFundsFrom(String accountNumber) {
        final WithdrawalRequest withdrawalRequest =
                new WithdrawalRequest(200, accountNumber, TRANSACTION_DESCRIPTION);

        withdrawingMoney.withdraw(withdrawalRequest);
    }

    private void depositFundsInto(String accountNumber) {
        final DepositRequest depositRequest = DepositRequestBuilder.aDepositRequest()
                .withAmount(300)
                .withAccountNumber(accountNumber)
                .withDescription(TRANSACTION_DESCRIPTION)
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
