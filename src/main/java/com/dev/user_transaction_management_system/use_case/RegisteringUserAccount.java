package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class RegisteringUserAccount {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisteringUserAccount(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = new UserMapper();
    }


    public void register(UserRegistrationRequest request) {
        ensureUserDoesNotExists(request.email());
        final String hashedPassword = passwordEncoder.encode(request.password());

        final UserId userId = UserId.fromUUID(UUID.randomUUID());
        final User user = User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                Credential.of(Email.of(request.email()), Password.fromHashedPassword(hashedPassword)));

        userRepository.save(user.toEntity());
    }

    private void ensureUserDoesNotExists(String userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
