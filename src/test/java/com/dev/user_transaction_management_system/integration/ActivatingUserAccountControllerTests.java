package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserAccountFixture;
import com.dev.user_transaction_management_system.domain.event.NotifiableEvent;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.dto.UserActivationRequest;
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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Tag("INTEGRATION")
@Transactional
class ActivatingUserAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountFixture userAccountFixture;

    @MockitoSpyBean
    @Qualifier("fakeEmailNotifier")
    private Notifier notifier;

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
        final String username = userAccount.email();
        final UserActivationRequest userActivationRequest = new UserActivationRequest(username);

        mockMvc.perform(post("/api/user/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(objectMapper.writeValueAsString(userActivationRequest)))
                .andExpect(status().isOk());

        final Optional<UserEntity> userEntity = userRepository.findByEmail(username);

        assertThat(userEntity).isPresent();
        assertThat(userEntity.get().getUserStatus()).isEqualTo(UserStatus.ENABLE);
        then(notifier).should(times(1)).send(any(NotifiableEvent.class));
    }
}
