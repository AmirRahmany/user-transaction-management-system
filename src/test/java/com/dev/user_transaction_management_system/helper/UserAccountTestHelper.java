package com.dev.user_transaction_management_system.helper;

import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.dev.user_transaction_management_system.domain.user.UserStatus;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.infrastructure.persistence.model.UserEntity;
import com.dev.user_transaction_management_system.infrastructure.util.UserMapper;

public class UserAccountTestHelper {

    private final UserRepository userRepository;

    public UserAccountTestHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public UserEntity havingRegistered(UserFakeBuilder userFakeBuilder) {
        final UserEntity entity = userFakeBuilder.build().toEntity();
        this.userRepository.save(entity);
        return entity;
    }

    public UserEntity havingActivate(UserFakeBuilder userFakeBuilder) {
        final UserEntity entity = havingRegistered(userFakeBuilder);
        entity.setUserStatus(UserStatus.ENABLE);
        userRepository.save(entity);
        return entity;
    }
}
