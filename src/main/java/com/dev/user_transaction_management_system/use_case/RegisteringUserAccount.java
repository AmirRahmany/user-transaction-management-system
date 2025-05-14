package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisteringUserAccount {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisteringUserAccount(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(UserRegistrationRequest request) {
        ensureUserDoesNotExistsWith(request.email());
        final String hashedPassword = passwordEncoder.encode(request.password());
        final UserId userId = UserId.fromUUID(userRepository.nextIdentify());

        final User user = User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                Credential.of(Email.of(request.email()), Password.fromHashedPassword(hashedPassword)));

        userRepository.save(user.toEntity());
    }

    private void ensureUserDoesNotExistsWith(String userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
