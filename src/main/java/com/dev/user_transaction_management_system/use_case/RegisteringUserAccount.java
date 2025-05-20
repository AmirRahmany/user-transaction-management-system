package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUserAlreadyExists;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.use_case.event.RegisteredUserAccount;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisteringUserAccount {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegisteringUserAccount(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = publisher;
    }


    @Transactional
    public void register(UserRegistrationRequest request) {
        ensureUserDoesNotExistsWith(request.email());
        final String hashedPassword = passwordEncoder.encode(request.password());
        final UserId userId = UserId.fromUUID(userRepository.nextIdentify());

        final User user = User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                Credential.of(Email.of(request.email()), Password.fromHashedPassword(hashedPassword)));

        userRepository.save(user.toEntity());
        eventPublisher.publishEvent(event(request, user));
    }

    private static RegisteredUserAccount event(UserRegistrationRequest request, User user) {
        return new RegisteredUserAccount(user.fullName(), request.email(), request.phoneNumber());
    }

    private void ensureUserDoesNotExistsWith(String userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw new CouldNotRegisterUserAlreadyExists();
        }
    }
}
