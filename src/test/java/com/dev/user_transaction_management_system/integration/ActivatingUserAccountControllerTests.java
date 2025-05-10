package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFindAccount;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.use_case.RegisteringUserAccount;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class ActivatingUserAccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisteringUserAccount registeringUserAccount;

    @Test
    void activate_user_account_successfully() throws Exception {
        final UserEntity entity = havingRegistered(aUser().withDisabledStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/user/activation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entity.getId())))
                .andExpect(status().isOk());

        final Optional<UserEntity> userEntity = userRepository.findById(entity.getId());
        assertThat(userEntity).isPresent();

        assertThat(userEntity.get().getUserStatus()).isEqualTo(UserStatus.ENABLE);
    }

    private UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserRegistrationRequest user = userFakeBuilder.buildDTO();
        registeringUserAccount.register(user);
        return userRepository.findByEmail(user.email()).orElseThrow(CouldNotFindAccount::new);
    }
}
