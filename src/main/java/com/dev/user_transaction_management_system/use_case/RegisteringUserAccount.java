package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.Calendar;
import com.dev.user_transaction_management_system.domain.Date;
import com.dev.user_transaction_management_system.domain.user.*;
import com.dev.user_transaction_management_system.domain.exceptions.CouldNotRegisterUser;
import com.dev.user_transaction_management_system.use_case.dto.UserRegistrationRequest;
import com.dev.user_transaction_management_system.domain.user.UserAccountWasRegistered;
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

    private final Calendar calendar;

    @Autowired

    public RegisteringUserAccount(@NonNull UserRepository userRepository,
                                  @NonNull PasswordEncoder passwordEncoder,
                                  @NonNull ApplicationEventPublisher publisher,
                                  @NonNull Calendar calendar) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = publisher;
        this.calendar = calendar;
    }


    @Transactional
    public void register(UserRegistrationRequest request) {
        Assert.notNull(request, "user registration request cannot be null");

        ensureUserDoesNotExistsWith(request.email());
        final String hashedPassword = passwordEncoder.encode(request.password());
        final UserId userId = userRepository.nextIdentify();
        final Date createdAt = calendar.today();

        final User user = openUserAccountFrom(request, userId, hashedPassword,createdAt);

        userRepository.save(user.toEntity());
        eventPublisher.publishEvent(event(request, user));
    }

    private static User openUserAccountFrom(UserRegistrationRequest request,
                                            UserId userId,
                                            String hashedPassword,
                                            Date date) {
        return User.of(userId,
                FullName.of(request.firstName(), request.lastName()),
                PhoneNumber.of(request.phoneNumber()),
                Credential.of(Email.of(request.email()), Password.fromHashedPassword(hashedPassword)),
                UserStatus.DISABLE,
                date);
    }

    private static UserAccountWasRegistered event(UserRegistrationRequest request, User user) {
        return new UserAccountWasRegistered(user.fullName(), request.email(), request.phoneNumber());
    }

    private void ensureUserDoesNotExistsWith(String userEmail) {
        if (userRepository.isUserAlreadyExists(userEmail)) {
            throw CouldNotRegisterUser.becauseUserAlreadyExisted();
        }
    }
}
