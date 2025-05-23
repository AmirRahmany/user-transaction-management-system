package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUser;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.domain.event.RegisteredUserAccount;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisteringUserAccount {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    @Autowired

    public RegisteringUserAccount(@NonNull UserRepository userRepository,
                                  @NonNull PasswordEncoder passwordEncoder,
                                  @NonNull ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = publisher;
    }


    @Transactional
    public void register(UserRegistrationRequest request) {
        Assert.notNull(request,"user registration request cannot be null");

        ensureUserDoesNotExistsWith(request.email());
        final String hashedPassword = passwordEncoder.encode(request.password());
        final UserId userId = UserId.fromUUID(userRepository.nextIdentify());

        final User user = openUserAccountFrom(request, userId, hashedPassword);

        userRepository.save(user.toEntity());
        eventPublisher.publishEvent(event(request, user));
    }

    private static User openUserAccountFrom(UserRegistrationRequest request, UserId userId, String hashedPassword) {
        return User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                Credential.of(Email.of(request.email()), Password.fromHashedPassword(hashedPassword)));
    }

    private static RegisteredUserAccount event(UserRegistrationRequest request, User user) {
        return new RegisteredUserAccount(user.fullName(), request.email(), request.phoneNumber());
    }

    private void ensureUserDoesNotExistsWith(String userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw CouldNotRegisterUser.becauseUserAlreadyExisted();
        }
    }
}
