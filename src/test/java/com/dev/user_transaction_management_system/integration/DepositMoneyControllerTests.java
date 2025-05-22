package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.DepositRequestBuilder;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.DepositRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
class DepositMoneyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @Autowired
    private BankAccountTestHelper bankAccountHelper;

    @MockitoSpyBean
    @Qualifier("fakeEmailNotifier")
    private Notifier emailNotifier;

    private User userAccount;
    private String token;

    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.givenActivatedSignedInUserWithToken();
        userAccount = userAndToken.user();
        token = userAndToken.token();
    }

    @Test
    void init_deposit_money_request_successfully() throws Exception {
        final BankAccount to = bankAccountHelper.havingOpened(anAccount().enabled().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final DepositRequest transferMoneyRequest = DepositRequestBuilder.aDepositRequest()
                .withAmount(300)
                .withAccountNumber(to.accountNumberAsString())
                .withDescription("transaction description")
                .initiate();

        mockMvc.perform(post("/api/transaction/deposit")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(transferMoneyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.referenceNumber").exists())
                .andExpect(jsonPath("$.data.accountNumber").exists())
                .andExpect(jsonPath("$.data.createdAt").exists());


        BankAccountEntity savedToAccount = bankAccountHelper.findByAccountNumber(to.accountNumber());
        assertThat(savedToAccount.getBalance()).isEqualTo(800);
        then(emailNotifier).should(atLeastOnce()).sendSimpleMessage(any(Subject.class),any(Message.class),any(Email.class));
    }
}
