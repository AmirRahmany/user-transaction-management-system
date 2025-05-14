package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisteringUserAccountTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisteringUserAccount registeringUserAccount;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_user_successfully() {
        var user = aUser().buildDTO();
        String userId= "8c5148ea-857b-4996-a09c-5a5131a33564";
        when(userRepository.nextIdentify()).thenReturn(UUID.fromString(userId));
        when(passwordEncoder.encode(user.password())).thenReturn("hashedPassword");
        doNothing().when(userRepository).save(any(UserEntity.class));


        assertThatNoException().isThrownBy(() -> registeringUserAccount.register(user));
        verify(passwordEncoder).encode(user.password());

        verify(userRepository).save(argThat(entity -> {
            assertThat(entity.getPassword()).isEqualTo("hashedPassword");
            assertThat(entity.getId()).isEqualTo(userId);
            return true;
        }));
    }

    @Test
    void can_not_register_user_without_any_name() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withFirstName(null).buildDTO()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withLastName(null).buildDTO()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withFirstName(" ").buildDTO()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withLastName(" ").buildDTO()));
    }

    @Test
    void can_not_register_user_without_any_phone_number() {
        System.out.println(UUID.randomUUID());
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withNullPhoneNumber()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withBlankPhoneNumber()));
    }

    @Test
    void can_not_register_user_without_valid_email() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withNullEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withBlankEmail()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withInvalidEmail()));
    }

    @Test
    void can_not_register_user_with_repetitive_email() {
        final String mail = "amirrahmani@gmail.com";

        when(userRepository.isUserAlreadyExists(mail)).thenReturn(true);

        final UserRegistrationRequest newUser = aUser().withEmail("amirrahmani@gmail.com").buildDTO();

        assertThatExceptionOfType(CouldNotRegisterUserAlreadyExists.class)
                .isThrownBy(() -> registeringUserAccount.register(newUser));

        verify(userRepository, never()).save(any());
    }

    @Test
    void can_not_register_user_without_any_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withNullPassword()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withEmptyPassword()));
    }

    @Test
    void can_not_register_user_with_invalid_password() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withPassword("12345").buildDTO()));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> registeringUserAccount.register(aUser().withPassword("12345678").buildDTO()));
    }

}
