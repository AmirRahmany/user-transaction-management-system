package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.UserTransactionManagementSystemApplication;
import com.dev.user_transaction_management_system.application.UserRegistration;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.model.UserEntity;
import com.dev.user_transaction_management_system.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.user_transaction_management_system.fake.UserFake.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = UserTransactionManagementSystemApplication.class)
@ActiveProfiles("test")
@Transactional
class UserRegistrationIntegrationTests {

    @Autowired
    private UserRegistration userRegistration;

    @Autowired
    private UserRepository userRepository;

    @Test
    void register_user_successfully() {
        final User user = user().build();

        userRegistration.register(user);

        UserEntity userSaved = userRepository.findByEmail(user.email()).orElse(null);

        assertThat(userSaved).isNotNull();
        assertThat(userSaved.firstName()).isEqualTo(user.firstName());
        System.out.println(userSaved.firstName());
    }

    @Test
    void can_not_register_user_with_repetitive_email() {
        final User user = user().withEmail("duplicate@email.com").build();

        userRegistration.register(user);

        assertThat(userRepository.findByEmail(user.email()).isPresent()).isTrue();
        assertThatExceptionOfType(CouldNotRegisterUserAlreadyExists.class)
                .isThrownBy(()->userRegistration.register(user));
    }
}
