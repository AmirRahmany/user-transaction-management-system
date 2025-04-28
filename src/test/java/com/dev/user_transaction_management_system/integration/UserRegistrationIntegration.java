package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserTransactionManagementSystemApplication;
import com.dev.user_transaction_management_system.application.UserRegistration;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.entity.UserEntity;
import com.dev.user_transaction_management_system.fake.UserFake;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserTransactionManagementSystemApplication.class)
@ActiveProfiles("test")
@Transactional
class UserRegistrationIntegration {

    @Autowired
    private UserRegistration userRegistration;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_user_successfully() {
        final User user = UserFake.user().build();

        userRegistration.register(user);

        UserEntity userSaved = userRepository.findByEmail(user.email()).orElse(null);

        assertThat(userSaved).isNotNull();
        assertThat(userSaved.firstName()).isEqualTo(user.firstName());
        System.out.println(userSaved.firstName());

    }
}
