package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRegistration userRegistration;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_user_successfully() {
        var user = aUser().build();


        when(passwordEncoder.encode(user.password())).thenReturn("hashedPassword");
        doNothing().when(userRepository).enroll(any(UserEntity.class));


        assertThatNoException().isThrownBy(() -> userRegistration.register(user));
        verify(passwordEncoder).encode(user.password());
        verify(userRepository).enroll(argThat(entity -> {
            assertThat(entity.getPassword()).isEqualTo("hashedPassword");
            return true;
        }));
    }

    @Test
    void can_not_register_user_without_any_name() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withFirstName(null).build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withLastName(null).build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withFirstName(" ").build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withLastName(" ").build()));
    }

    @Test
    void can_not_register_user_without_any_phone_number() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withNullPhoneNumber().build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withBlankPhoneNumber().build()));
    }

    @Test
    void can_not_register_user_without_valid_email() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withNullEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withBlankEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withInvalidEmail()));
    }

    @Test
    void can_not_register_user_with_repetitive_email() {
        final String mail = "amirrahmani@gmail.com";

        when(userRepository.isUserAlreadyExists(mail)).thenReturn(true);

        final User newUser = aUser().withEmail("amirrahmani@gmail.com").build();

        assertThatExceptionOfType(CouldNotRegisterUserAlreadyExists.class)
                .isThrownBy(() -> userRegistration.register(newUser));

        verify(userRepository, never()).enroll(any());
    }

    @Test
    void can_not_register_user_without_any_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withNullPassword()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withEmptyPassword()));
    }

    @Test
    void can_not_register_user_with_invalid_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withPassword("12345").build()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> userRegistration.register(aUser().withPassword("12345678").build()));
    }

}
