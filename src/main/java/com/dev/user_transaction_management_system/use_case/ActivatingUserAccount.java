package com.dev.user_transaction_management_system.use_case;

import com.dev.user_transaction_management_system.domain.exceptions.CouldNotFoundUser;
import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.domain.user.UserId;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;
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

    public void activate(String userId) {
        final UserEntity userEntity = userRepository.findById(UserId.fromString(userId))
                .orElseThrow(() -> CouldNotFoundUser.withId(userId));
        final User user = userMapper.toDomain(userEntity);

        if (user.isDisable()){
            user.enabled();
            userRepository.save(user.toEntity());
        }
    }
}
