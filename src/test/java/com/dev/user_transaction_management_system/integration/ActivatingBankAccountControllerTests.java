package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.bank_account.AccountNumber;
import com.dev.user_transaction_management_system.domain.bank_account.AccountStatus;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccount;
import com.dev.user_transaction_management_system.domain.bank_account.BankAccountRepository;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.helper.BankAccountTestHelper;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.BankAccountEntity;
import com.dev.user_transaction_management_system.use_case.dto.BankAccountActivationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.BankAccountFakeBuilder.anAccount;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Import(UserAccountFixture.class)
@Tag("INTEGRATION")
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
    private UserAccountFixture userAccountFixture;

    @MockitoSpyBean
    @Qualifier("fakeEmailNotifier")
    private Notifier notifier;


    private String token;
    private User userAccount;

    @BeforeEach
    void setUp() {
        var userWithToken = userAccountFixture.havingRegisteredUserWithToken("amirrahmani7017@gmail.com", "@Abcd137854");
        token = userWithToken.token();
        userAccount = userWithToken.user();
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


        then(notifier).should(atLeastOnce()).sendSimpleMessage(any(Subject.class),any(Message.class),any(Email.class));
    }

}
