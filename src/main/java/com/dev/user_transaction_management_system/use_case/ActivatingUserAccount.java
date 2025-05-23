package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.mapper.UserMapper;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ActivatingUserAccount {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final ApplicationEventPublisher eventPublisher;

    public ActivatingUserAccount(@NonNull UserRepository userRepository,
                                 @NonNull ApplicationEventPublisher publisher,
                                 @NonNull UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.eventPublisher = publisher;
    }

    @Transactional
    public void activate(String email) {
        Assert.hasText(email, "username cannot be null or empty");
        final User user = finUserBy(email);

        user.enabled();

        userRepository.save(user.toEntity());
        user.releaseEvents().forEach(eventPublisher::publishEvent);
    }

    private User finUserBy(String username) {
        final UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> CouldNotFoundUser.withId(username));
        return userMapper.toDomain(userEntity);
    }
}
