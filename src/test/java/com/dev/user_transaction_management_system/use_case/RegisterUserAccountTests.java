package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.fake.FakeClock;
import com.dev.user_transaction_management_system.fake.FakeEventPublisher;
import com.dev.user_transaction_management_system.fake.PasswordEncoderStub;
import com.dev.user_transaction_management_system.fake.UserRepositoryFake;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUser;
import com.dev.user_transaction_management_system.use_case.register_user_account.UserRegistrationRequest;
import com.dev.user_transaction_management_system.use_case.register_user_account.RegisterUserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RegisterUserAccountTests {

    private RegisterUserAccount registerUserAccount;

    @BeforeEach
    void setUp() {
        this.registerUserAccount = new RegisterUserAccount(
                new UserRepositoryFake()
                , new PasswordEncoderStub(),
                new FakeEventPublisher(),
                new FakeClock());
    }

    @Test
    void register_user_successfully() {

        final UserRegistrationRequest registrationRequest = new UserRegistrationRequest("Ali",
                "rezaiee",
                "ali@gmail.com",
                "@Abcd1234",
                "09112427000");

        assertThatNoException().isThrownBy(() -> registerUserAccount.register(registrationRequest));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"","  "})
    void can_not_register_user_without_any_first_name(String firstName) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withFirstName(firstName).buildDTO()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"","  "})
    void can_not_register_user_without_any_last_name(String lastName) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withLastName(lastName).buildDTO()));
    }

    @Test
    void can_not_register_user_without_any_phone_number() {
        System.out.println(UUID.randomUUID());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withNullPhoneNumber()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withBlankPhoneNumber()));
    }

    @Test
    void can_not_register_user_without_valid_phone_number() {
        final String invalidPhoneNumber = "09112379";
        final UserRegistrationRequest registrationRequest = aUser().withPhoneNumber(invalidPhoneNumber).buildDTO();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(registrationRequest));
    }

    @Test
    void can_not_register_user_without_valid_email() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withNullEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withBlankEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withInvalidEmail()));
    }

    @Test
    void can_not_register_user_with_repetitive_email() {
        final String mail = "jacid20853@besibali.com";
        final UserRegistrationRequest newUser = aUser().withEmail(mail).buildDTO();

        registerUserAccount.register(newUser);

        assertThatExceptionOfType(CouldNotRegisterUser.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withEmail(mail).buildDTO()));
    }

    @Test
    void can_not_register_user_without_any_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withNullPassword()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withEmptyPassword()));
    }


    @ParameterizedTest(name = "password")
    @ValueSource(strings = {"12345","abc12345","ABC45678","@abc123456"})
    void can_not_register_user_with_invalid_password(String password) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registerUserAccount.register(aUser().withPassword(password).buildDTO()));
    }
}
