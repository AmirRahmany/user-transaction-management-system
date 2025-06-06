package com.dev.user_transaction_management_system.use_case.register_user_account;

import com.dev.user_transaction_management_system.domain.Clock;
import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUser;
import com.dev.user_transaction_management_system.domain.user.UserAccountWasRegistered;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegisterUserAccount {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    private final Clock clock;

    @Autowired

    public RegisterUserAccount(@NonNull UserRepository userRepository,
                               @NonNull PasswordEncoder passwordEncoder,
                               @NonNull ApplicationEventPublisher publisher,
                               @NonNull Clock clock) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = publisher;
        this.clock = clock;
    }


    @Transactional
    public void register(UserRegistrationRequest request) {
        Assert.notNull(request, "user registration request cannot be null");

        final var email = Email.of(request.email());
        final var password = Password.fromPlainPassword(request.password());
        ensureUserDoesNotExistsWith(email);

        final String hashedPassword = passwordEncoder.encode(password.toString());
        final UserId userId = userRepository.nextIdentify();

        final var credential = Credential.of(email, Password.fromHashedPassword(hashedPassword));
        final User user = openUserAccountFrom(request, userId,credential, clock.currentTime());

        userRepository.save(user.toEntity());
        eventPublisher.publishEvent(event(request, user));
    }

    private static User openUserAccountFrom(UserRegistrationRequest request,
                                            UserId userId,
                                            Credential credential,
                                            LocalDateTime currentTime) {
        return User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                credential,
                UserStatus.DISABLE,
                Date.fromCurrentTime(currentTime));
    }

    private static UserAccountWasRegistered event(UserRegistrationRequest request, User user) {
        return new UserAccountWasRegistered(user.fullName(), request.email(), request.phoneNumber());
    }

    private void ensureUserDoesNotExistsWith(Email userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw CouldNotRegisterUser.becauseUserAlreadyExisted();
        }
    }
}
