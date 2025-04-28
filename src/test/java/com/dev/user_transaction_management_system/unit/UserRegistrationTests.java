package com.dev.user_transaction_management_system.unit;

import com.dev.user_transaction_management_system.application.UserRegistration;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.entity.UserEntity;
import com.dev.user_transaction_management_system.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.helper.UserMapper;
import com.dev.user_transaction_management_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.dev.user_transaction_management_system.fake.UserFake.user;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRegistration userRegistration;

    private UserMapper userMapper;


    @BeforeEach
    void setUp() {
        this.userMapper = new UserMapper();
    }

    @Test
    void register_user_without_any_throw_exception() {
        var user = user().build();

        doNothing().when(userRepository).enroll(any(UserEntity.class));

        assertThatNoException().isThrownBy(() -> userRegistration.register(user));
        verify(userRepository).enroll(userMapper.toEntity(user,0));
    }

    @Test
    void can_not_register_user_without_any_name() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withFirstName(null).build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().lastName(null).build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withFirstName(" ").build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().lastName(" ").build()));
    }

    @Test
    void can_not_register_user_without_any_phone_number() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withNullPhoneNumber().build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withBlankPhoneNumber().build()));
    }

    @Test
    void can_not_register_user_without_valid_email() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withNullEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withBlankEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withInvalidEmail()));
    }

    @Test
    void can_not_register_user_with_repetitive_email() {
        final String mail = "amirrahmani@gmail.com";
        var existingUser = user().withEmail(mail).build();

        when(userRepository.findByEmail(mail)).thenReturn(Optional.of(userMapper.toEntity(existingUser,0)));

        final User newUser = user().withEmail("amirrahmani@gmail.com").build();

        assertThatExceptionOfType(CouldNotRegisterUserAlreadyExists.class)
                .isThrownBy(() -> userRegistration.register(newUser));

        verify(userRepository, never()).enroll(any());
    }

    @Test
    void can_not_register_user_without_any_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withNullPassword()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().withEmptyPassword()));
    }

    @Test
    void can_not_register_user_with_invalid_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().password("12345").build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(user().password("12345678").build()));
    }

}
