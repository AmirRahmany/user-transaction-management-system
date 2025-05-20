package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.infrastructure.util.EmailListener;
import com.dev.user_transaction_management_system.infrastructure.util.EmailNotifier;
import com.dev.user_transaction_management_system.use_case.dto.BankAccountActivationRequest;
import com.dev.user_transaction_management_system.use_case.event.BankAccountActivated;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Tag("INTEGRATION")
@Transactional
class ActivatingBankAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankAccountRepository accountRepository;

    @Autowired
    private BankAccountTestHelper accountHelper;

    @Autowired
    private UserAccountTestUtil userAccountUtil;

    @MockitoSpyBean
    private EmailNotifier notifier;


    private String token;
    private User userAccount;

    @BeforeEach
    void setUp() {
        String username="amir@gmail.com";
        String password="@Abcd137854";

        userAccount = userAccountUtil.havingRegistered(aUser().withEmail(username).withPassword(password));
        token = userAccountUtil.signIn(username, password);
    }


    @Test
    void activate_user_bank_account_successfully() throws Exception {
        final BankAccount bankAccount = accountHelper.havingOpened(anAccount().withUser(userAccount));

        final BankAccountActivationRequest activationRequest =
                new BankAccountActivationRequest(bankAccount.accountNumberAsString());

        mockMvc.perform(post("/api/account/activate")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization",token)
                .content(objectMapper.writeValueAsString(activationRequest)))
                .andExpect(status().isOk());

        AccountNumber accountNumber = bankAccount.accountNumber();
        final Optional<BankAccountEntity> savedBankAccount = accountRepository.findByAccountNumber(accountNumber);

        assertThat(savedBankAccount).isPresent();
        assertThat(savedBankAccount.get().getStatus()).isEqualTo(AccountStatus.ENABLE);


        then(notifier).should(times(1)).send(any(),any());
    }

}
