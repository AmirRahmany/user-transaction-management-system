package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.EmailNotifier;
import com.dev.user_transaction_management_system.use_case.dto.WithdrawalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Tag("INTEGRATION")
class WithdrawingMoneyControllerTests{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @Autowired
    private BankAccountTestHelper bankAccountHelper;

    @MockitoSpyBean
    private EmailNotifier emailNotifier;


    private String token;
    private User userAccount;

    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.givenActivatedSignedInUserWithToken();
        token = userAndToken.token();
        userAccount = userAndToken.user();
    }

    @Test
    void withdraw_money_transaction_successfully() throws Exception {
        var account = bankAccountHelper.havingOpened(anAccount().withUser(userAccount)
                .withAccountNumber("0300654789123").withBalance(500));

        final WithdrawalRequest withdrawalRequest =
                new WithdrawalRequest(300, account.accountNumberAsString(), "description");

        mockMvc.perform(post("/api/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(withdrawalRequest)))
                .andExpect(status().isOk());


        BankAccountEntity savedToAccount = bankAccountHelper.findByAccountNumber(account.accountNumber());
        assertThat(savedToAccount.getBalance()).isEqualTo(200);
        then(emailNotifier).should(atLeastOnce()).send(any(NotifiableEvent.class));
    }
}
