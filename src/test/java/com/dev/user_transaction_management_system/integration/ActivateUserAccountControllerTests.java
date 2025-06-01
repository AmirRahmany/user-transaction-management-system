package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.activate_user_account.UserActivationRequest;
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

import java.util.Optional;

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
class ActivateUserAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @MockitoSpyBean
    @Qualifier("fakeEmailNotifier")
    private Notifier emailNotifier;

    @Autowired
    private ObjectMapper objectMapper;

    private User userAccount;
    private String token;

    @BeforeEach
    void setUp() {
        var userAndToken = userAccountFixture.havingRegisteredUserWithToken("amirrhmani7017@gmail.com", "@Abcd137728");
        userAccount = userAndToken.user();
        token = userAndToken.token();
    }


    @Test
    void activate_user_account_successfully() throws Exception {
        final UserActivationRequest userActivationRequest = new UserActivationRequest(userAccount.email().asString());

        mockMvc.perform(post("/api/user/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(userActivationRequest)))
                .andExpect(status().isOk());

        final Optional<UserEntity> userEntity = userRepository.findByEmail(userAccount.email());

        assertThat(userEntity).isPresent();
        assertThat(userEntity.get().getUserStatus()).isEqualTo(UserStatus.ENABLE);
        then(emailNotifier).should(atLeastOnce()).sendSimpleMessage(any(Subject.class),any(Message.class),any(Email.class));
    }
}
