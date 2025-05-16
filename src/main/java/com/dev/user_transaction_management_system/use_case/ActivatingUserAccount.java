package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
import io.jsonwebtoken.lang.Assert;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ActivatingUserAccount {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ActivatingUserAccount(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
    }

    public void activate(String username) {
        Assert.hasText(username,"user id cannot be null or empty");

        final User user = finUserBy(username);
        if (user.isDisable()){
            user.enabled();
            userRepository.save(user.toEntity());
        }
    }

    private User finUserBy(String username) {
        final UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> CouldNotFoundUser.withId(username));
        return userMapper.toDomain(userEntity);
    }
}
